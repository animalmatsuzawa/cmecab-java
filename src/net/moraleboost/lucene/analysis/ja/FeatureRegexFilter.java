/*
 **
 **  Mar. 22, 2008
 **
 **  The author disclaims copyright to this source code.
 **  In place of a legal notice, here is a blessing:
 **
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 **
 **                                         Stolen from SQLite :-)
 **  Any feedback is welcome.
 **  Kohei TAKETA <k-tak@void.in>
 **
 */
package net.moraleboost.lucene.analysis.ja;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class FeatureRegexFilter extends TokenFilter
{
    private Pattern[] patterns = null;
    private Matcher[] matchers = null;

    /**
     * トークンのタイプ属性
     */
    private TypeAttribute typeAttribute = null;
    /**
     * トークンの位置増加分属性
     */
    private PositionIncrementAttribute posIncAttribute = null;

    /**
     * typeが指定したパターンに合致するtokenをふるい落とすフィルタを構築する。
     * 
     * @param input
     *            上流TokenStream
     * @param stopPatterns
     *            Java正規表現の配列を指定。
     *            typeがこのパターンのいずれかにマッチするtokenはフィルタリングされる。
     */
    public FeatureRegexFilter(TokenStream input, String[] stopPatterns)
    {
        super(input);
        buildPatterns(stopPatterns);
        typeAttribute = (TypeAttribute)addAttribute(TypeAttribute.class);
        posIncAttribute = (PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class);
    }

    private void buildPatterns(String[] stopPatterns)
    {
        patterns = new Pattern[stopPatterns.length];
        matchers = new Matcher[stopPatterns.length];
        
        for (int i = 0; i < stopPatterns.length; ++i) {
            patterns[i] = Pattern.compile(stopPatterns[i]);
            matchers[i] = null;
        }
    }

    /**
     * tokenのtypeが構築時に指定したパターンのいずれかにマッチするかどうかを調べる。
     * 
     * @param token
     *            トークン
     * @return いずれかのパターンにマッチすればtrue。全くマッチしなければfalse。
     */
    private boolean match(String feature)
    {
        Matcher m = null;
        
        for (int i = 0; i < matchers.length; ++i) {
            m = matchers[i];
            if (m == null) {
                m = patterns[i].matcher(feature);
                matchers[i] = m;
            } else {
                m.reset(feature);
            }

            if (m.matches()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean incrementToken() throws IOException
    {
        int skippedPositions = 0;
        while (input.incrementToken()) {
            if (!match(typeAttribute.type())) {
                posIncAttribute.setPositionIncrement(
                        posIncAttribute.getPositionIncrement() + skippedPositions);
                return true;
            }
            skippedPositions += posIncAttribute.getPositionIncrement();
        }
        
        return false;
    }
    
    public void reset() throws IOException
    {
        super.reset();
        clearAttributes();
    }
}

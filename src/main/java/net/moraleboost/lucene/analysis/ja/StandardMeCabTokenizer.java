/*
 **
 **  May. 17, 2009
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

import java.io.Reader;
import java.io.IOException;

import net.moraleboost.mecab.Lattice;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.Tagger;

/**
 * MeCabを用いて入力を分かち書きするTokenizerのベース。
 * <br><br>
 * 生成されるTokenのtermには、形態素の表層形が格納される。
 * typeには、形態素の素性が格納される。
 * 
 * @author taketa
 *
 */
public class StandardMeCabTokenizer extends Tokenizer
{
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final int DEFAULT_MAX_SIZE = 10 * 1024 * 1024;

    private int maxSize;

    private Tagger tagger;
    private Lattice lattice;
    private Node node;
    private int offset;

    /**
     * トークンのターム属性
     */
    private CharTermAttribute termAttribute;
    /**
     * トークンのオフセット属性
     */
    private OffsetAttribute offsetAttribute;
    /**
     * トークンのタイプ属性
     */
    private TypeAttribute typeAttribute;

    /**
     * オブジェクトを構築する。
     *
     * @param in 入力
     * @param tagger 形態素解析器
     * @param maxSize 入力から読み込む最大文字数(in chars)
     * @throws IOException
     */
    public StandardMeCabTokenizer(Reader in, Tagger tagger, int maxSize)
    throws IOException
    {
        this(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY, in, tagger, maxSize);
    }

    /**
     * オブジェクトを構築する。
     *
     * @param factory 使用するAttributeFactory
     * @param in 入力
     * @param tagger 形態素解析器
     * @param maxSize 入力から読み込む最大文字数(in chars)
     * @throws IOException
     */
    public StandardMeCabTokenizer(AttributeFactory factory, Reader in, Tagger tagger, int maxSize)
    throws IOException
    {
        super(factory, in);

        this.maxSize = maxSize;
        
        this.tagger = tagger;

        termAttribute = addAttribute(CharTermAttribute.class);
        offsetAttribute = addAttribute(OffsetAttribute.class);
        typeAttribute = addAttribute(TypeAttribute.class);
        
        parse();
    }
    
    protected Tagger getTagger()
    {
        return tagger;
    }
    
    @Override
    public final boolean incrementToken() throws IOException
    {
        if (node == null || node.stat() == Node.TYPE_EOS_NODE) {
            lattice.clear();
            return false;
        }

        clearAttributes();

        String[] leadingSpaceAndSurface = new String[2];
        if (!node.leadingSpaceAndSurface(leadingSpaceAndSurface)) {
            throw new IOException("Can't get leading space and surface from node.");
        }
        String tokenString = leadingSpaceAndSurface[1];
        String blankString = leadingSpaceAndSurface[0];
        int start;
        int end;

        if (blankString != null) {
            start = offset + blankString.length();
            end = start + tokenString.length();
        } else {
            start = offset;
            end = start + tokenString.length();
        }

        offset = end;

        termAttribute.setEmpty();
        termAttribute.append(tokenString);
        offsetAttribute.setOffset(
                correctOffset(start),
                correctOffset(end));
        typeAttribute.setType(node.feature());

        node = node.next();
        return true;
    }
    
    @Override
    public void end()
    {
        int finalOffset = correctOffset(offset);
        offsetAttribute.setOffset(finalOffset, finalOffset);
    }

    @Override
    public void reset() throws IOException
    {
        offset = 0;
        try {
            if (lattice != null) {
                lattice.destroy();
            }
        } finally {
            node = null;
            lattice = null;
        }
        clearAttributes();
        parse();
    }

    @Override
    public void close() throws IOException
    {
        try {
            if (lattice != null) {
                lattice.destroy();
            }
        } finally {
            node = null;
            lattice = null;
            super.close();
        }
    }

    private void parse() throws IOException
    {
        lattice = tagger.createLattice();

        // drain input
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        StringBuilder builder = new StringBuilder(DEFAULT_BUFFER_SIZE);
        long total = 0;
        int nread;
        while (-1 != (nread = input.read(buffer))) {
            builder.append(buffer, 0, nread);
            total += nread;
            if (total > maxSize) {
                throw new IOException("Max size exceeded.");
            }
        }

        // parse
        lattice.setSentence(builder.toString());
        if (!tagger.parse(lattice)) {
            throw new IOException(lattice.what());
        }
        node = lattice.bosNode();
        if (node != null) {
            node = node.next();
        }
    }
}
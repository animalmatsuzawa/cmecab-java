CMeCab

1. ����͉��H

���{��`�ԑf��̓G���W��MeCab (http://mecab.sourceforge.net/) ��
Java�o�C���f�B���O�ł��BSWIG��p�����AJNI���璼��MeCab�̃��C�u������
�Ăяo���Ă��܂��B

���܂��Ƃ��āA�ȉ��̂��̂��܂�ł��܂��B
 * Lucene (http://lucene.apache.org/java/) �p��Tokenizer�A
   �f������p���ăg�[�N�����t�B���^�����O����TokenFilter�A
   ����т�����g�ݍ��킹��Analyzer
 * Solr (http://lucene.apache.org/solr/) �p��TokenizerFactory
   �����TokenFilterFactory


2. �p�b�P�[�W�\��

�z�z�p�b�P�[�W�́A�ȉ��̃f�B���N�g���\���������܂��B

bin  - �r���h���ꂽJava���C�u�����������o�����f�B���N�g��
jni  - �l�C�e�B�u���C�u�����̃\�[�X�R�[�h���i�[���ꂽ�f�B���N�g��
lib  - �r���h����уe�X�g�ɕK�v�ȃT�[�h�p�[�e�B���C�u������
      �i�[���ꂽ�f�B���N�g��
src  - Pure Java���C�u�����̃\�[�X�R�[�h���i�[���ꂽ�f�B���N�g��
test - �e�X�g�p�f�[�^���i�[���ꂽ�f�B���N�g��


3. �C���X�g�[�����@

CMeCab�́A�ȉ��̓�̃p�[�g����Ȃ�܂��B
 * MeCab��Java�̋��n��������l�C�e�B�u���C�u����
 * �l�C�e�B�u���C�u������p���ē��삷��Pure Java���C�u����

�ȉ��A���ꂼ��̃C���X�g�[�����@�ɂ��Đ������܂��B

3.1. �l�C�e�B�u���C�u�����̃C���X�g�[��

jni�f�B���N�g���Ɉړ����Amake�����s���Ă��������B
Windows���Visual Studio��p����̂ł���΁A�ȉ��̃R�}���h�����s���܂��B

% nmake -f Makefile.win

Linux���make�����gcc�𗘗p����ꍇ�́A�ȉ��̃R�}���h�����s���܂��B

% make -f Makefile.unix

�Ȃ��A�eMakefile���ɂ́A��҂̃r���h���ɂ�����Java�����MeCab��
�p�X���L�ڂ���Ă��܂��B�K�v�ɉ����āACMECAB_INCLUDE�ACMECAB_LIB��
���������Ă��������B

�r���h���I������ƁA�J�����g�f�B���N�g���ɁACMeCab.dll(Windows)�A
��������libCMeCab.so(UNIX�n)���쐬����܂��B�����OS�̃p�X�̒ʂ����ꏊ��
�R�s�[���Ă��������B

3.2. Java���C�u�����̃C���X�g�[��

�z�z�p�b�P�[�W�̃��[�g�f�B���N�g���ŁAant�����s���Ă��������B

% ant

�r���h���I������ƁAbin�f�B���N�g���ɁAcmecab-(�o�[�W�����ԍ�).jar
�Ƃ������O��JAR�t�@�C�����쐬����܂��B��������D���ȏꏊ�ɃR�s�[���āA
Java�̃N���X�p�X��ʂ��Ă��������B


4. ���p���@
http://code.google.com/p/cmecab-java/wiki/HowToUse
���������������B


5. ���C�Z���X

CMeCab�{�̂̓p�u���b�N�h���C���Ƃ��܂��B

�Ȃ��A�r���h����уe�X�g�̂��߁Alib�f�B���N�g���Ɉȉ��̃\�t�g�E�F�A��
�������Ă��܂��B�����̃\�t�g�E�F�A�́A���ꂼ��̃��C�Z���X�ɏ]���܂��B

* Apache Lucene 2.4-dev
   * Apache License 2.0
* Apache Solr 1.3
   * Apache License 2.0
* JUnit 4.4
   * Common Public License 1.0


6. �A����

MeCab�ALucene�ASolr�{�̂Ɋւ��邲����́A
���ꂼ��̃\�t�g�E�F�A�̃��[�����O���X�g���ւǂ����B

CMeCab���̂Ɋւ��邲���ⓙ�́A���c���� k-tak@void.in �܂łǂ����B


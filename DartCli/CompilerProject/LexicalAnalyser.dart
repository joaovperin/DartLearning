///
/// A Lexical analyser for a compiler
///
class LexicalAnalyser {
  
  static const int T_ID = 1;
  static const int T_OPERATOR = 2;
  static const int T_VALUE = 3;

  String lookAhead;
  int token;
  String lexema;
}

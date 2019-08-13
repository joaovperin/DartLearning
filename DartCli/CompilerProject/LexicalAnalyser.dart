///
/// A Lexical analyser for a compiler
///
class LexicalAnalyser {
  String lookAhead;
  int token;
  String lexema;
  int pointer;
  String currentSourceLine;
  String currentLine;
  int currentColumn;
  String errorMessage;

  List<String> identifiedTokens = [];

}

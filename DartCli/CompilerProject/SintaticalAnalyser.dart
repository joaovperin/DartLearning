import 'dart:io';
import 'dart:convert';
import 'dart:async';

import 'Tokens.dart';

///
/// A Sintatical analyser for a compiler
///
class SintaticalAnalyser {
  int currentLine;
  int currentColumn;
  int pointer = 0;
  String line = "";
  Tokens token = Tokens.T_NULL;

  void run() {
    final file = new File('res/input.py');
    var lines = file.readAsLinesSync();
    lines.forEach((l) => print(l));
  }
}

void main(List<String> args) {
  SintaticalAnalyser().run();
}

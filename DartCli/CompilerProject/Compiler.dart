import 'CodeGenerator.dart';
import 'LexicalAnalyser.dart';
import 'SemanticAnalyser.dart';
import 'SintaticalAnalyser.dart';

///
/// A (self-explaned) Compiler.
///
/// It's made of lexical, syntactical (a lot of types) and semantic analysis,
/// along with a code generator (with internal optimizers).
///
class Compiler {
  LexicalAnalyser lexicalAnaliser;
  SintaticalAnalyser sintaticalAnaliser;
  SemanticAnalyser semanticAnaliser;
  CodeGenerator codeGenerator;
}

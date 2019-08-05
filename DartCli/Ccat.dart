import 'dart:io';
import 'dart:convert';

void main(List<String> args) {
  print('Hello, World!');
  if (args?.isEmpty) {
    stdin.pipe(stdout);
  } else {
    print(args);
  }
}

import 'dart:html';
import 'dart:core';

// Application
App app;
// output
final Element screen = querySelector('#output');

/// A class that represent a todo task
class TodoTask {
  static int count = 0;

  int id;
  String message;
  DateTime createdAt;
  DateTime finishedAt;

  TodoTask(String msg) {
    this.id = ++count;
    this.message = msg;
  }

  @override
  String toString() {
    return '$id) $message';
  }
}

/// Main entry point
void main() {
  app = App()..run();
}

class TaskRepository {
  Element ct;
  InputElement taskNew;
  List<Element> tasksCt;

  TaskRepository() {
    this.ct = screen.querySelector("#tasks-container");
    this.tasksCt = this.ct.querySelector('#task-array').children;
    this.taskNew = this.ct.querySelector("#task-new")
      ..onChange.listen((Event evt) {
        var v = this.taskNew.value;
        if (v.trim().isNotEmpty) {
          this.add(TodoTask(this.taskNew.value));
          this.taskNew.value = '';
        }
      });
  }

  void add(TodoTask task) {
    LIElement newTaskElement = LIElement();
    this.tasksCt.add(newTaskElement
      ..children.add(SpanElement()
        ..text = task.toString()
        ..setAttribute('style', 'float:left'))
      ..children.add(SpanElement()
        ..setAttribute('style', 'float:right')
        ..children.add(SpanElement()
          ..text = 'C'
          ..setAttribute('class', 'green')
          ..onClick.listen((e) => queryElement(newTaskElement)))
        ..children.add(SpanElement()
          ..text = 'A'
          ..setAttribute('class', 'blue')
          ..onClick.listen((e) => alterElement(newTaskElement)))
        ..children.add(SpanElement()
          ..text = 'X'
          ..setAttribute('class', 'red')
          ..onClick.listen((e) => removeElement(newTaskElement)))));
  }

  void queryElement(elm) {}
  void alterElement(elm) {}
  void removeElement(elm) => {elm.remove()};
}

/// Application
class App {
  TaskRepository dao = TaskRepository();
  void run() {
    dao.add(TodoTask("testandooo"));
    dao.add(TodoTask("testandooodfdfdf"));
  }
}

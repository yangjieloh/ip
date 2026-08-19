# UI Test Plan

Run each test case in a fresh instance of `Pixel`. Output comparisons are exact except that CRLF and LF line endings are considered equivalent. Each command below is followed by Enter.

## UI-01: Add and list tasks

**Aim:** Verify that Pixel stores entered task descriptions, lists them in order as not done, and exits on `bye`.

**Input commands:**

```text
read book
return book
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
added: read book
____________________________________________________________
____________________________________________________________
added: return book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[ ] read book
2.[ ] return book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## UI-02: Mark and unmark a task

**Aim:** Verify that `mark` completes the selected task and `unmark` reverses its done status.

**Input commands:**

```text
read book
return book
mark 2
list
unmark 2
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
added: read book
____________________________________________________________
____________________________________________________________
added: return book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [X] return book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[ ] read book
2.[X] return book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [ ] return book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[ ] read book
2.[ ] return book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## UI-03: Add and manage typed tasks

**Aim:** Verify that Pixel adds ToDos, Deadlines, and Events; treats date/time values as strings; lists each task with its type icon; and preserves typed-task formatting when marking and unmarking.

**Input commands:**

```text
todo borrow book
deadline do homework /by no idea :-p
event project meeting /from Mon 2pm /to 4pm
mark 2
list
unmark 2
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] do homework (by: no idea :-p)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] do homework (by: no idea :-p)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][X] do homework (by: no idea :-p)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] do homework (by: no idea :-p)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] do homework (by: no idea :-p)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## UI-04: Reject malformed dated tasks

**Aim:** Verify that incomplete Deadline and Event commands show usage guidance without terminating Pixel or adding invalid tasks.

**Input commands:**

```text
deadline do homework
deadline /by Sunday
event project meeting /from Monday
event project meeting /from /to Friday
todo valid task
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____  _          _ 
|  _ \(_)_  _____| |
| |_) | \ \/ / _ \ |
|  __/| |>  <  __/ |
|_|   |_/_/\_\___|_|
Hello! I'm Pixel.
What can I do for you?
____________________________________________________________
____________________________________________________________
Please use: deadline DESCRIPTION /by DATE/TIME
____________________________________________________________
____________________________________________________________
Please use: deadline DESCRIPTION /by DATE/TIME
____________________________________________________________
____________________________________________________________
Please use: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Please use: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] valid task
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] valid task
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

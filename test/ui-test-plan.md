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

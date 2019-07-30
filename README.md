# ProjectLuria
### What is ProjectLuria?
This is the implementation of an interpreter in Java for a simple C-like high-level-language named Luria. This is to fulfil requirements of an MSc.
### Luria
Luria is a simple C-like high-level language.

It is dynamically typed; variables can represent a number (e.g. 1, 1.0, -1), a String (e.g. "0", "hello", "0world"), or a boolean (i.e. true, false).

In valid Luria syntax we can declare variables as follows:

variable x;
x = 1;
x = "hello";
x = false;
x = 1 + 1;

Luria allows for:
#### identifiers
Their associated token is SIGNIFIER and can consist of an alphanumeric sequence of characters, except they cannot begin with a numerical digit, e.g. '0variableName'. An example of an identifier is 'x', e.g. 'variable x;' or 'variable x = 0;'.
#### reserved words
Each reserved word or keyword in Luria is mapped to an associated String and token, and is responsible for representing specific Luria functionalities, e.g. the sequence 'and' is mapped to an AND token with the functionality of '&&' in Java, for example. The reserved keywords in Luria are:
##### one char tokens
'('   ')'   '{'   '}'   ','   '.'   '-'   '+'   ';'   '/'   '*'
##### one or many char tokens
'!'   '='   '>'   '<'   '!='   '=='   '<='   '>='
##### reserved words
'and'
'or'
'variable'
'if'
'else'
'while'
'function'

#### Example instructions
variable x = true;
if (x != false) {
  print "Hello, world.";
  }
  else { print x;
  }

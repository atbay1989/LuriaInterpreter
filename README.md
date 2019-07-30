# ProjectLuria
### What is ProjectLuria?
This is an implementation of an interpreter in Java for a simple high-level-language named Luria. This is part of a development project to fulfil the requirements of an MSc in Software Development at the University of Glasgow.
### Luria
Luria is a simple high-level language. It's syntax is C-like, but differs in that it is dynamically typed.

Variables can represent a number (e.g. 1, 1.0, -1), a String (e.g. "0", "hello", "0world"), or a boolean (i.e. true, false). Numbers are 'handled' by the interpreter as doubles; numbers ending ".0" are truncated upon their printing, e.g. "1.0" becomes "1".

In valid Luria syntax we can declare variables as follows:

    variable x;
    x = 1;
    x = "hello";
    x = false;
    x = 1 + 1;

Luria supports the elementary arithmetic operators addition, subtraction, multiplication, and division. 

Valid expressions include:

    1 + 1; // 2
    1 - 1; // 0
    2 * 2; // 4
    3 / 2; // 1.5
    0 / 2; // 0

The addition operator can also be applied to strings:

    variable x = "Hello, ";
    variable y = "world!";
    print x + y; // Hello, world!

Unary operators permit the expression of negative numbers and their binary operations are handled correctly:

    -1 + 1; // 0
    -1 + -1; // -2
    -1 - -1; // 2
    -2 * -2; // 4
    2 / -2; // -1

Boolean logic is supported in Luria. and is expressed and unary operator ! represents NOT:

    variable x = true
    variable y = false
    !x; // false
    x == y; // true
    x != y; // false
    x and y; // false
    x or y; // true

If then else conditions are written:

    variable x = true;
    variable y = true;
        if (x) print "current";
    else print "affairs" // current

The 'dangling else' problem is handled by the convention that the else is associated with the if that immediately precedes it:

    if (x) if (y) print "current"
        else print "affairs" // current

To declare a function:

    function square(x) {
        return x * x;
    }

A function call then be called:

    print square(x);

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

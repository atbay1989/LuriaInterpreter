# ProjectLuria
ProjectLuria is an implementation of an interpreter in Java for a simple high-level-language named Luria. This is part of a development project to fulfil the requirements of an MSc in Software Development at the University of Glasgow.

---

## Luria
Luria is a simple, imperative high-level language. It's syntax is C-like, with the exception that it is dynamically typed and (as of yet) cannot read and interpret user input, among many other functionalities.

NB: Like C, a single-line character sequence prefixed with // is interpreted as a comment:

    // this is a comment

---

### Data Types & Variables
Variables, declared using the "variable" keyword, can represent a number (e.g. 1, 1.0, -1), a string (e.g. "0", "hello", "0world"), a boolean (i.e. true, false), or null, a value representing the absence of a value. When declared with no value assigned, a variable defaults to null.

Numbers are handled by the interpreter as doubles; numbers ending ".0" are truncated upon their printing, e.g. "1.0" becomes "1".

In valid Luria syntax we can declare variables as follows:

    variable x; // null
    x = 1; // 1
    x = "hello"; // hello
    x = false; // false
    x = 1 + 1.0; // 2

Variables declared can be operated upon as the values assigned and can also be reassigned.

    variable x = 0; // 0
    x = x + 1; // 1
 
---

### Statements & Expressions
#### Statements
Statements are syntactic units in Luria that express a command to carry out an action. In Luria, the is no distinction between statements and declarations. Every statement must end with a semi-colon. Luria supports the following statements:
    
    1 + 1; // an expression statement
    
    variable pi = 3.1415926535; // a variable declaration
    
    print pi * (3 * 3); // a print statement
    
    if (pi == 3.1415926535) print "true"; else print "false"; // an if (and           
    associated) then else statement
    
    while (true) print "infinite loop" // a while statement with a condition in       
    parentheses, followed by its (single) command
    
    { print "increment"; x = x + 1; } // a block statement, delineating areas of       
    scope and permitting association of a series of statements, for example, to a     
    while command.
    
    function square(x) { print x * x; } // a function declaration, featuring its       
    signifier ("square"), argument(s) ("(x)"), and function block.
    
    return x * x; // a return statement, permitting a function to return a value,     
    rather than exist only as a sub-routine.
    
#### Arithmetic Expressions
##### Binary Operations
Luria supports the elementary arithmetic operators addition, subtraction, multiplication, and division; modulo is not yet supported. These binary operators are applied in *infix* notation. 

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

##### Unary Operations
Unary operators permit the expression of negative numbers and their binary operations are handled correctly. The minus symbol can be interpreted *prefix* to negate a number and *infix* to form a subtraction expression.

    -1 + 1; // 0
    -1 + -1; // -2
    -1 - -1; // 2
    -2 * -2; // 4
    2 / -2; // -1

#### Precedence
The conventional order of operations is followed, whereby multiplication and division precede addition and subtraction. Thus:

    12 / 3 * 4 - 1; // 15, i.e. ((12 / 3) * 4) - 1

Luria also permits the grouping of expressions using parentheses:

    12 / 6 - 2; // 0
    12 / (6 - 2); // 3

#### Logical Operations
Boolean logic is supported in Luria. and is expressed and unary operator ! represents NOT:

    variable x = true
    variable y = false
    !x; // false
    x == y; // true
    x != y; // false
    x and y; // false
    x or y; // true
    
---

### Control Flow
If then else conditions are written:

    variable x = true;
    variable y = true;
        if (x) print "current";
    else print "affairs" // current

The 'dangling else' problem is handled by the convention that the else is associated with the if that immediately precedes it:

    if (x) if (y) print "current"
        else print "affairs" // current

---

### Functions
To declare a function:

    function square(x) {
        return x * x;
    }

A function can then be called:

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

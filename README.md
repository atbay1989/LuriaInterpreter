# ProjectLuria
ProjectLuria is an implementation of an interpreter in Java for a simple, imperative high-level-language named Luria. This is part of a development project to fulfil the requirements of an MSc in Software Development at the University of Glasgow.

---
## Luria
Luria is a simple, imperative high-level language.

Like many C-family programming languages, a single-line character sequence prefixed with **//** is interpreted as a comment:

    // this is a comment in Luria

Here is an example of Luria syntax, demonstrating some of its features:

    function fibonacci(n) {
      if (n <= 1) return n;
      return fibonacci(n - 2) + fibonacci(n - 1);
      }
    variable i = 0;
    while (i < 20) {
	print fibonacci(i);
	i = i + 1;
    }

---
### Data Types & Variables
#### Data Types & Variables
Variables, declared using the "variable" keyword, can represent a number (e.g. **1**, **1.0**, **-1**), a string (e.g. **"0"**, **"hello"**, **"0world"**), a boolean (i.e. **true**, **false**), an array (e.g. **[0,1,2]**, **["a","b","c"]**) or **null**, a value representing the absence of a value. When declared with no value assigned, a variable defaults to **null**. Luria is therefore is *dynamically typed*.

Numbers are handled by the interpreter as Java doubles; numbers ending **".0"** are truncated upon their printing, e.g. **"1.0"** becomes **"1"**, except when printing an array, in which case they remain presented as doubles.

In valid Luria syntax variables can be declared and reassigned as follows:

    variable x; // null
    x = 1; // 1
    x = "hello"; // hello
    x = false; // false
    x = 1.0; // 1

Variables declared can be operated upon as their assigned value and can also be reassigned.

    variable x = "that's ";
    x + "nice" // that's nice
    variable x = 0
    x = x + 1; // 1

#### Arrays
Arrays in Luria are also *dynamic* and can be assigned any combination of data types. They are indexed from 0 and the valued stored can be accessed, assigned and reassigned. Luria supports *multidimentional* arrays. At present, an array is of fixed length and when declared a value must be given at each index. Examples of Luria array syntax are:

    variable a = [0,0,3,0,0];
    variable b = ["a", 1, true, null, empty, [0,1]];
    b[0]; // a
    b[1] - 1 // 0
    b[3] = "full" // full
    b[4][2]; // 3
    
---
### Statements & Expressions
#### Statements
Statements are syntactic units that express a command to carry out an action. Like many C-family languages, every statement in Luria must end with a semi-colon, **;**, else an error is thrown at runtime. Luria supports the following example statements:
    
    x = x + 1; // an expression statement, such as an increment
    
    variable pi = 3.1415926535; // a variable declaration
    
    print pi * (3 * 3); // a print statement
    
    if (pi == 3.1415926535) print "true"; else print "false"; // an if (and           
    // associated) then and else statement
    
    while (true) print "infinite loop" // a while statement with a condition in       
    // parentheses, followed by its (single) command
    
    { print "decrement"; x = x - 1; } // a block statement, delineating areas of       
    // lexical scope and allowing for association of a sequence of statements, for            
    // example, to a while command.
    
    function square(x) { print x * x; } // a function declaration, featuring its       
    // signifier ("square"), parameter(s) ("(x)"), and function block.
    
    return x * x; // a return statement, permitting a function to return a value,     
    // rather than exist only as a sub-routine.
    
    readnumber x // a readnumber statement, interrupting the program to await user input
    // of a number value to reassign a variable, in this case x
    
    readstring x // a readstring statement, interrupting the program to await user input
    // of a string value
    
    readboolean x // a readboolean statement, interrupting the program to await user input
    // of a boolean value
    
##### Read Statements
Because Luria is *dynamically typed*, provided are three means of accepting user input, each for assigning a new value to either a number, boolean or string variable. Read statements block interpretation of the program until a user inputs a value, for example;

    variable aString = "sad";
    variable aNumber = 1;
    variable aBoolean = false;
    readstring aString;
    // happy
    // aString = happy;
    readnumber aNumber;
    // 10 
    // aNumber = 10;
    readboolean aBoolean;
    // true
    // aBoolean = true

#### Arithmetic Expressions
##### Binary Operations
Luria supplies the elementary arithmetic operators addition, subtraction, multiplication, and division. Modulo and exponent are also supported. These binary operators are expressed in *infix* notation. 

Valid expressions include:

    1 + 1; // 2
    1 - 1; // 0
    2 * 2; // 4
    3 / 2; // 1.5
    0 / 2; // 0
    5 % 2; // 1
    2 ^ 2; // 4

The addition operator can also be applied to strings:

    variable x = "Hello, ";
    variable y = "world!";
    print x + y; // Hello, world!

##### Unary Operations
Luria supports unary operators for the expression of negative numbers; their binary operations are handled correctly. The minus symbol (**-**) can be interpreted either in *prefix* to negate a number and *infix* to form a subtraction expression:

    -1 + 1; // 0
    -1 + -1; // -2
    -1 - -1; // 2
    -2 * -2; // 4
    2 / -2; // -1

The exclamation mark symbol (**!**) negates a truth value:

    !true; // false
    !x // false
    !(1 != 1) // true

##### Precedence
The conventional order of operations is followed, whereby multiplication and division precede addition and subtraction. Thus:

    12 / 3 * 4 - 1; // 15, i.e. ((12 / 3) * 4) - 1

Luria also permits the combination of expressions using parentheses:

    12 / 6 - 2; // 0
    12 / (6 - 2); // 3

---
### Comparison & Equality Operations
Boolean logic is supported in Luria. **true** and **false** values can be assigned to variables. Boolean values can also be derived from comparison operations:

    1 < 2; // true
    2 > 1; // true
    1 >= 2; // false
    2 <= 1; // false
    1 == 2; // false
    1 != 2; // true

#### Logical Operations
Also available are logical operators **!**, **and**, and **or**. If an operand is true, **!**, i.e. not, returns a false value and vice versa.

    variable x = true;
    !x; // false

**and** and **or** are eqivilants to the **&&** and **||** symbols in most C-family languages and determine whether two values or expressions are both true or if either values or expressions are true, respectively.

---
### Lexical Scope & Control Flow
#### Lexical Scope
Lexical scope is expressed by the encapsulation of statements between left and right braces, **{*** and **}**, forming block statements. Not only does this allow for the declaration and access of variables at different levels of scope, but they aid the construction of more control flow statements and are essential to the definition of functions:

    variable a = "global";
    {
        variable a = "block";
        print a;
    }
    print a;
    // prints block followed by global on a new line
    
#### Control Flow
Control flow is made possible with the available if... else and loop type statements. Both constructs involve evaluation of logical expressions, i.e. conditions. In both examples below, "*print "current";*" are *then* statements. Examples of **if** and **if... else** in Luria are:

    if ("a" != "b") print "current"; // current
    
    if ("a" != "a") print "current";
        else print "affairs"; // affairs

The 'dangling else' problem is handled by the C language convention that the **else** is associated with the **if** that immediately precedes it, for example:

    if (true) if (false) print "current"; else print "affairs"; // affairs
        
This construct can also be represented using braces, affording lexical scope and greater readability:

    if (true) {
        if (false) {
            print "current";
            print "!";
        } else {
            print "affairs";
            print "!"// affairs followed by ! on a new line
        
Looping is achieved using **while**. Like **if**, **while** requires a condition to evaluate and can be expressed without braces (to execute a single command) or with braces (to execute one or many statements). The following is implementation of a for type loop in Luria using **while**:

    variable i = 0;
    while (i <= 10) {
        print i;
        i = i + 1;
    } // returns numbers 1 to 10 each on a new line

---
### Functions
Functions are declared in Luria using the keyword **function** followed by a function signifier, its parameter(s) expressed as variable signfier(s) in parentheses, followed by the function block. For example: 

    function square(x) {
        print x * x;
    }

A function can then be called:

    square(4); // 16

Important, but not essential to functions in Luria are **return** statements, which allow for the return of a value from a function. Functions can therefore act as operands in expressions.

    function square(x) {
        return x * x;
    }
    variable pi = 3.1415926535;
    variable area = pi * square(3);
    print area; // 28.2743338815
        
In this way, functions can evaluate to a value and can therefore be passed as arguments to other functions:

    function square(x) {
        return x * x;
    }
    variable pi = 3.1415926535;
    function areaOfACircle(n) {
        return pi * n;
    }
    print areaOfACircle(square(3)); // 28.2743338815
    
---

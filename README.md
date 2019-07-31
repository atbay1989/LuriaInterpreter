# ProjectLuria
ProjectLuria is an implementation of an interpreter in Java for a simple, imperative high-level-language named Luria. This is part of a development project to fulfil the requirements of an MSc in Software Development at the University of Glasgow.

---

## Luria
Luria is a simple, imperative high-level language. It's syntax is C-like, with the exception that it is dynamically typed and (as of yet) cannot read and interpret user input, among other functionalities.

NB: Like C, a single-line character sequence prefixed with **//** is interpreted as a comment:

    // this is a comment in Luria

---

### Data Types & Variables
Variables, declared using the "variable" keyword, can represent a number (e.g. **1**, **1.0**, **-1**), a string (e.g. **"0"**, **"hello"**, **"0world"**), a boolean (i.e. **true**, **false**), or **null**, a value representing the absence of a value. When declared with no value assigned, a variable defaults to **null**.

Numbers are handled by the interpreter as doubles; numbers ending **".0"** are truncated upon their printing, e.g. **"1.0"** becomes **"1"**.

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
Statements are syntactic units that express a command to carry out an action. In Luria, the is no distinction between statements and declarations. Like C, every statement in Luria must end with a semi-colon, **;**, else an error is thrown at runtime. Luria supports the following statements:
    
    1 + 1; // an expression statement
    
    variable pi = 3.1415926535; // a variable declaration
    
    print pi * (3 * 3); // a print statement
    
    if (pi == 3.1415926535) print "true"; else print "false"; // an if (and           
    // associated) then else statement
    
    while (true) print "infinite loop" // a while statement with a condition in       
    // parentheses, followed by its (single) command
    
    { print "increment"; x = x + 1; } // a block statement, delineating areas of       
    // lexical scope and permitting association of a series of statements, for            
    // example, to a while command.
    
    function square(x) { print x * x; } // a function declaration, featuring its       
    // signifier ("square"), argument(s) ("(x)"), and function block.
    
    return x * x; // a return statement, permitting a function to return a value,     
    // rather than exist only as a sub-routine.
    
#### Arithmetic Expressions
##### Binary Operations
Luria supplies the elementary arithmetic operators addition, subtraction, multiplication, and division; modulo is not yet supported. These binary operators are expressed in *infix* notation. 

Valid expressions include:

    1 + 1; // 2
    1 - 1; // 0
    2 * 2; // 4
    3 / 2; // 1.5
    0 / 2; // 0

NB: The addition operator can also be applied to strings:

    variable x = "Hello, ";
    variable y = "world!";
    print x + y; // Hello, world!

##### Unary Operations
Unary operators permit the expression of negative numbers and their binary operations are handled correctly. The minus symbol (**-**) can be interpreted *prefix* to negate a number and *infix* to form a subtraction expression.

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

**and** and **or** are eqivilants to **&&** and **||** in C and determine whether two values or expressions are both true or if either values or expressions are true, respectively.

---
### Lexical Scope & Control Flow
#### Lexical Scope
Lexical scope is expressed by the encapsulation of statements between left and right braces, **{*** and **}**, forming block statements. Not only does this allow for the declaration and access of variables at different levels of scope, but aid the construction of control flow statements and are essential to the definition of functions:

    variable a = "global";
    {
        variable a = "block";
        print a;
    }
    print a;
    // prints block followed by global on a new line
    
#### Control Flow
Control flow is made possible with the available if... else and loop type statements. Both constructs involve evaluation of logical expressions, i.e. conditions. Examples of **if** and **if... else** in Luria are:

    if ("a" != "b") print "current"; // current
    
    if ("a" != "a") print "current";
        else print "affairs"; // affairs

The 'dangling else' problem is handled by the convention that **else** is associated with the **if** that immediately precedes it:

    if (true) if (false) print "current";
        else print "affairs"; // affairs
        
This construct can also be represented using braces, affording lexical scope and greater readability:

    if (true) {
        if (false) {
            print "current";
        } else {
            print "affairs"; // affairs
        
Looping is achieved using **while**. Like **if**, **while** requires a condition to evaluate and can be expressed without braces (to execute a single command) or with braces (to execute one or many statements). The following is implementation of a for type loop in Luria using **while**:

    variable i = 0;
    while (i <= 10) {
        print i;
        i = i + 1;
    } // returns numbers 1 to 10 each on a new line

---

### Functions
Functions are declared in Luria using the keyword **function** followed by a function signifier, its argument(s) expressed as variable signfier(s) in parentheses, followed by the function block. For example: 

    function square(x) {
        print x * x;
    }

A function can then be called:

    square(4); // 16

Integral, but not essential to functions in Luria are **return** statements, which allow for the return of a value from a function. Functions can therefore act as operands in expressions.

    function square(x) {
        return x * x;
    }
    variable pi = 3.1415926535;
    variable area = pi * square(3);
    print area; // 28.2743338815
        
In this way, functions in Luria are *first class* and can be passed as arguments to other functions:

    function square(x) {
        return x * x;
    }
    variable pi = 3.1415926535;
    function areaOfACircle(n) {
        return pi * n;
    }
    print areaOfACircle(square(3)); // 28.2743338815
        
NB: A function call containing a print statement with execute print twice. It is presumed that there exists two executed statements: first the print statement existing within the function and the statement that calls the function.

---

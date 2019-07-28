# ProjectLuria
### What is ProjectLuria?
This is the implementation of an interpreter in Java for a simple C-like high-level-language named Luria.
### Luria
Luria is a simple C-like high-level language.

Luria allows for:
#### identifiers
Their associated token is SIGNIFIER and can consist of an alphanumeric sequence of characters, except they cannot begin with a numerical digit, e.g. '0variableName'. An example of an identifier is 'x', e.g. 'variable x;' or 'variable x = 0;'.
#### reserved words
Each reserved word or keyword in Luria is mapped to an associated String and token, and is responsible for representing specific Luria functionalities, e.g. the sequence 'and' is mapped to an AND token with the functionality of '&&' in Java, for example. The reserved keywords in Luria are:
##### one char tokens
'('
')'
'{'
'}'
','
'.'
'-'
'+'
';'
'/'
'*'
##### one or many char tokens
'!'
'='
'>'
'<'
'!='
'=='
'<='
'>='
##### reserved words
'and'
'variable'
'if'
'else'
'while'

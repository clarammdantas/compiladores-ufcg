program teste;

procedure soma(x,y:integer);
var
z: integer;
begin
    z := x + y;
end;

function soma_func(x, y:integer):integer;
var
z: integer;
begin
    z := x + y;
    soma_func := z;
end;

var 
a: string;
i, soma_res: integer;
arr: array[1..10] of boolean;

begin
    a := 'Hello, world!';
    i := 10;
    
    repeat
        arr[i] := i > 3;
        i := i - 1;
    until i = 1;
    
    soma(1, 2);
    soma_res := soma_func(1, 2);
end.

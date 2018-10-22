program teste;

procedure soma(x, y: integer);
var
z: integer = 0;
begin
    z := x + y;
end;

function soma_func(var x, y: integer): integer;
var
z: integer;
begin
    z := x + y;
    soma_func := z;
end;

var 
a: string;
i, soma_res: integer;

begin
    a := 'Can''t stop, won''t stop!';
    i := 10;
    
    soma(1, 2);
    soma_res := soma_func(1, 2);
end.

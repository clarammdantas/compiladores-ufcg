program test;

procedure compute(x, y: integer);
var
	z: integer = -+-+25;
begin
    z := y - -((-x / +y)) * (+y % -x);
end;

function soma(var x, y: integer): integer;
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
    
    repeat
        i := i - 1;
    until i = 1;

    compute(1, 2);
    soma_res := soma(1, 2);
end.

program test;

procedure compute(x, y: integer);
var
	z: integer = -+-+25;
begin
    z := y - -((-x / +y)) * (+y mod -x);
end;

function soma(x, y: integer; var ref: string): integer;
var
	z: integer;
begin
    z := x + y;
    soma := z;
end;

var 
	a: string;
	i, soma_res: integer;
	arr: array[1..10] of boolean;
	matrix: array of array of integer;

begin
    a := 'A'#66 + #67#68'E'#70;
    a := 'Can''t stop, won''t stop!';
    i := 10;
    
    repeat
        arr[i] := i > 3;
        matrix[i, 10-i] := matrix[10-i, i];
        i := i - 1;
    until i = 1;

    compute(1, 2);
    soma_res := soma(1, 2, a);
end.

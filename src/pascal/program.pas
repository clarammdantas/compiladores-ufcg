program test;

var x, y: array[1..4] of array[0..4] of integer;
var z: integer;

begin
	x[4, 1] := 65;
	y[2, 2] := x[4, 1];
	z := y[2, 2];
end.

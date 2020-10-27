Triangle Drawing and Rotation

----DESCRIPTION----

This java swing program lets the user click 3 locations to draw the triangle that
would be formed by these vertices. Each vertex has a square handle attached to it
to allow the user to click and drag to redraw the triangle through rubberbanding.
There is a button which starts an animation for rotationg the triangle 360 degrees.
The reset button clears the canvas and lets the user start over with picking new vertices.


BUGS

-Rotation does not work correctly

-For some reason the triangle changes shape as it rotates and does not rotate around the origin as expected
possibly something is faulty with the centroid calculation or the casting of variables but I was not able to find
the exact issue. The triangle also moves and disappears into the upper left corner of the screen as it rotates.

-Also, the stop button and reset button are not stopping the animation as expected but the reset button is correctly
clearing the previously inputted data points

-The project is otherwise working and functional
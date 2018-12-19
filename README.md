# ImageNation

Small project that was made to make it easier for APCSA students to experiment with image manipulation.

The original APCSA assignment that inspired this project was somewhat unwieldy, 
requiring students to specify the image file to load and statically compile all functions they intend to run on it.

This project instead allows students to load images after the program is started, 
as well as select one of many runtime loaded functions that they write. 
They can choose to run any function in any order to see the effect, as well as undo and redo actions.

There is a color manipulation API that allows students to adjust the color against multiple color spaces, including:
- RGB
- HSL
- HSV
- HCL

The API stores the last used "mode", 
and makes a best attempt to reduce artifacting that occurs from converting between color spaces.
That is to say, if all functions are only dealing with HSV, the API won't convert color to RGB and back over and over.

The interface also allows fluid panning and zooming, 
and gives helpful information like pixel position and the values of the various color properties, 
such as the values of red, green, blue,
as well as all the values of all the other dimensions of the other color spaces.

Finally the program allows students to easily export any processed images to a file, at any point.

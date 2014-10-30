# README #

This is a simple geographic visualization of election results over the past 50 years. Using data collected from the US Census Bureau and the US Geological survey, we can map the proportion of votes a candidate received form each county of the United States in the Presidential elections

### How do I get set up? ###

* Compile the 'HelloWorldExample.java' file (Intentionally named like that :) )
* Set the class path variable to the servlet.jar or compile the application the same way you would compile any other Servlet app
* Use the following URL to run the app

prefix_url = http://wherever.you.have.deployed.your.app/PurpleAmerica/index/go?region=USA-county&year=2012&width=1920&height=1080

You can modify the 'region' argument to 'CA' or 'NJ' to get state specific map.
Similarly year can be modified to any election year in between 1960 & 2012
Width and Height specify the dimensions of the image you want in the response

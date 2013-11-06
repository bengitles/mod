Moving Objects Database
By Ben Gitles
ETH Zurich
Mobile and Personal Information System
Skeleton code provided by Michael Nebeling

This is a Moving Objects Database that seeks to implement some of the main ideas presented in the article by Gace Trajcevski et al. entitled "Managing Uncertainty in Moving Objects Databases."

Implememtned Classes:

-GPSDevice: Once started, it returns a random position within the bounds of [0,100] for Latitude, Longitude, and Altitude, each one occurring after some integer x seconds, where x is in [1,5].

-GPSLine: A GPSLine contains two points and their corresponding timestamps (normalized to start time = 0). Its isOnLine method works directly in conjunction with getDate in MovingObject. The uncertainty distance is 3.0.

-GPSPosition: Each position is defined by three doubles: Latitude, Longitude, and Altitude

-MovingObject: This is the core of the program. See the class for descriptions of each method.

-Vector: This is used in conjunction with GPSLine to help calculate the distance from a point to a line segment.

I attempted to implement the db4o, but I was unsuccessful. I also ran into unexplained problems when trying to run the program in the ExampleMain class. I would appreciate feedback on what I did wrong here and how I can fix it.

This program required the download of db4o: http://www.db4o.com/
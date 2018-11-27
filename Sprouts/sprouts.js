//
//  Sprouts Game Code
//  For WWUSprouts 2015
//

/*  v1.0
    by Craig Kyle - 5/22/2015
    
    - Initial Release.
*/

/*  v1.1
    by Craig Kyle - 5/23/2015
    
    - Rewrote the way the circle objects interact with the code so that there is only one circle per circle object rather than a second, overlapping one.
    - Rewrote the pathEndPointsChecker() to instead check and see if both endpoints of the user created path are in some circle.
    + Added the pathIntersectionsChecker() to prevent users from drawing a path, starting in a circle and going through mutliple circles before ending in some different circle.
    + Added the maxIntersectionChecker() to prevent users from drawing a path to a circle that already has 3 paths connected to it.
    + Added error messages for the above, new checks and rewrote existing error messages for imroved clarity.
    + Added the finishedCircleColorer() function to color nodes with 3 paths connected to them as green.
    + Added text objects for the Gameover/Newgame/Exit screen.
    + Added the gameoverChecker() to check when the game is finished, and if it is, stop all user input and put up the Gameover/Newgame/Exit screen.
    - Changed the colors of the paths and circles for better visual effect
    + Added functionality to the 'New Game' text item so that when selected refreshes the browser and thus starting a new game.
    * Miscellaneous changes/bug fixes.
*/

/*  v1.2
    by Craig Kyle 5/25/15
    
    - Changed the path and circle objects to instead be initialized at the beginning of the program.
    - Optimized the finishedCircleColorer() function to utilize the new path and circle initializations to cut down on code. (~80% of code cut)
    - Optimized the gameoverChecker() function to utilize the new path and circle initializations to cut down on code. (~60% of code cut)
    - Optimized the maxIntersectionChecker() function to utilize the new path and circle initializations to cut down on code. (~80% of code cut)
    - Optimized the pathCollisionChecker() function to utilize the new path and circle initializations to cut down on code. (~80% of code cut)
    - Optimized the circleCollisionChecker() function to utilize the new path and circle initializations to cut down on code. (~80% of code cut)
    - Optimized the circleIntersectionsChecker() function to utilize the new path and circle initializations to cut down on code. (~70% of code cut)
    - Optimized the pathEndPointsChecker() function to utilize the new path and circle initializations to cut down on code. (~60% of code cut)
    - Optimized the pathIntersectionsChecker() function to utilize the new path and circle initializations to cut down on code. (~80% of code cut)
    + Added the pathsChecker() and circlesChecker() to enable a cut down on code in the onMouseUp() function.
    - Optimized the onMouseUp() function to utilize the new pathsChecker() and circlesChecker() to cut down on code. (~75% of code cut)
    - Renamed the variables of the all the check functions to make more sense.
    * Miscellaneous changes/bug fixes.
*/

/*  v1.3
    by Brock Haugen - 5/27/15
    
    - Changed the initialization and checking functions to work iteratively over an array of points/paths
    - This should increase overall performance
    - Cuts down on another ~800 lines of code
    - Nodes (circles in this case) are now positioned and sized dynamically
        - Essentially a game could be played with any number of starting nodes of any set size
    + Added gameInit(n) where n is the number of starting nodes
        - Call this at any time to reset the game and begin playing
*/

/*  v1.4
    by Craig Kyle 5/28/15
    
    - Fixed the pathCollisionChecker() to now work as intended.
    - Fixed the gameoverChecker() to now work intended.
    - Fixed the circleIntersectionsChecker() to work as intended.
    + Added a global variable called 'maxGame' which equals the formula for determining the maximum number of moves in a game.
        - Using the variables 'maxGame' and 'turn' I edited the onMouse functions to now prevent the player from drawing if the game is over.
    + Added a global variable called 'minGame' which equals the formula for determining the minimum number of moves in a game.
    - If the game ends early (before 'maxGame') the game will now incriment the variable 'turn' to a point past the maxGame.
    - Changed the NewGame/Exit button objects to Restart/MainMenu.
    - Added SoundFX for when the user makes an illegal move and when the game ends.
    * Miscellaneous changes/bug fixes.
*/

/*  v1.5
    by Craig Kyle 6/1/15
    
    + Added slightly smaller invisible circles that are centered in the visible circles.
    - Rewrote the onMouseDown and onMouseDrag functions to start drawing when your drag out of the smaller circles.
    + Added the playerTurn() function and playerTurn text items to tell which player's turn it is.
    * Miscellaneous changes/bug fixes.
    
*/

/*  v1.6
    by Brock Haugen 6/3/15
    
    + Added button to toggle sound.
    * Miscellaneous changes/bug fixes.
*/

/*  v1.7
    by Craig Kyle and Brock Haugen 6/4/15
    
    + Added temporary solution to deadNodes issue.
    * Miscellaneous changes/bug fixes.
*/

/*  v1.8
    by Craig Kyle 6/4/15
    
    - Rewrote the gameoverChecker() to accommodate for deadNodes in a better fashion.
    - Rewrote the onMouseDrag function to now always start the path within the start and end circle.
    * Miscellaneous changes/bug fixes.
*/


// Global variables declarations:
var cx = document.getElementById("game").width/4;
var cy = document.getElementById("game").height/4;
var r = 15;
var mx = 20;
var circleColor = "brown";
var doneColor = "green";
var deadColor = "black";
var turn, circles, paths, deadNodes, mousePos;
var scale = 5;
var nodes = prompt("Number of starting nodes:",3); // Change this to be user input
var maxGame = (3)*(nodes)-1;
var minGame = (2)*(nodes);
var sound = false;
var gameEndSoundFX = new Audio('https://www.myinstants.com/media/sounds/win31.mp3');
var errorSoundFX = new Audio('https://www.myinstants.com/media/sounds/erro.mp3');
var circleDone = 0;

// Initialization of the game:
function gameInit(n) {
    circles = [];
    workcircles = [];
    paths = [];
    deadNodes = [];
    turn = 0;
    for (var i = 0; i < n; i++) {
        var x = cx+Math.cos(Math.PI*2/n*i)*r*scale;
        var y = cy+Math.sin(Math.PI*2/n*i)*r*scale;
        circles.push( new Path.Circle(new Point(x,y), r) );
        workcircles.push( new Path.Circle(new Point(x,y), r - 1) );
        circles[i].fillColor = circleColor;
        playerTurn1.content = 'Player 1';
    }
}

// System for always correctly displaying whose turn it is:
function playerTurn() {
    if (turn % 2 !== 0) {
        playerTurn1.content = '';
        playerTurn2.content = 'Player 2';
    } else {
        playerTurn1.content = 'Player 1';
        playerTurn2.content = '';
    }
}

// A way to access the mouses current location (x,y) at all times:
function onMouseMove(event) {
    mousePos = event.point;
}

// Temporary solution for dead nodes:
function onKeyDown(event) {
    if(event.key == 'space') {
        for (var c in circles)
            if (circles[c].contains(mousePos))
				if (circles[c].fillColor == circleColor) {
                	deadNodes.push(c);
                	circles[c].fillColor = deadColor;
            	}
        if (gameoverChecker()) {
            if (sound)
                gameEndSoundFX.play();
            playerTurn1.content = '';
            playerTurn2.content = '';
            gameoverScreen.content = 'Game Over';
            turn = turn + maxGame;
        }
    }
}

// When the user presses down on the mouseclick, a new path object is created at the position of the mouse:
function onMouseDown(event) {
    errorMsg.content = '';
    if (turn < maxGame) 
        paths.push( new Path({strokeColor: doneColor}) );
}

// While the user holds down on the mouseclick, points are added to the path already initialized:
function onMouseDrag(event) {
    var location = 0;
    if (turn < maxGame) {
        for (var c in circles)
            if (circles[c].contains(event.point))
                location = c;
        if (workcircles[location].contains(event.point)){
			if (circleDone == 0){
            	var nearestPoint = workcircles[location].getNearestPoint(event.point);
				paths[paths.length-1].add(nearestPoint);
				circleDone = circleDone + 1;
			}
		} else {
			paths[paths.length-1].add(event.point);
			circleDone = 0;
		}
    }
}

// A check for making sure the path starts in a circle and ends in a circle:
function pathEndPointsChecker() {
    var p = paths.length-1;
    var begpt = paths[p].length / 1;
    var endpt = paths[p].length / paths[p].length;
    var begxy = paths[p].getPointAt(begpt);
    var endxy = paths[p].getPointAt(endpt);
    var begcheck = 0;
    var endcheck = 0;
    for (var i in circles) {
        begcheck += circles[i].contains(begxy);
        endcheck += circles[i].contains(endxy);
    }
    return (begcheck == 1 && endcheck == 1);
}

// A check for making sure that the user created path does not intersect any other path:
function pathCollisionChecker() {
    var intersections = 0;
    for (var i in paths) {
        for (var j = 0; j <= i; j++){
            intersections += paths[j].getIntersections(paths[i]).length;
            if (intersections !== 0)
                return false;
        }
    }
    return true;
}

// A check for making sure the circle generated by the users path does not collide with any other circles:
function circleCollisionChecker() {
    var intersections = 0;
    for (var i in circles) {
        for (var j = 0; j <= i; j++){
            intersections += circles[j].getIntersections(circles[i]).length;
            if (intersections !== 0)
                return false;
        }
    }
    return true;
}

// A check for making sure the circle generated by the users path has no more than 2 paths intersecting with it:
function circleIntersectionsChecker() {
    var intersections = 0;
    for (var j in paths)
        intersections += paths[j].getIntersections(circles[circles.length - 1]).length;
    if (intersections !== 2)
        return false;
    return true;
}

// A check for making sure the path drawn by the user does not passthrough a circle:
function pathIntersectionsChecker() {
    var intersections = 0;
    for (var c in circles)
        intersections += circles[c].getIntersections(paths[paths.length - 1]).length;
    if (intersections !== 2)
        return false;
    return true;
}

// A check for making sure the circles have no more than 3 path intersections:
function maxIntersectionChecker() {
    for (var c in circles) {
        var intersections = 0;
        for (var p in paths)
            intersections += circles[c].getIntersections(paths[p]).length;
        if (intersections > 3)
            return false;
    }
    return true;
}

// Colors Circles with 3 intersections as Green:
function finishedCircleColorer() {
    for (var c in circles) {
        var intersections = 0;
        for (var p in paths)
            intersections += circles[c].getIntersections(paths[p]).length;
        if (intersections == 3)
            circles[c].fillColor = doneColor;
    }
}

// A check for the system to know when the game is over:
function gameoverChecker() {
	var intersections = 0;
	if (turn == maxGame)
		return true;
	for (var c in circles)
		for (var d in deadNodes)
			if (deadNodes[d] == c)
				for (var p in paths)
					intersections -= circles[c].getIntersections(paths[p]).length;
	for (var c in circles)
		for (var p in paths)
			intersections += circles[c].getIntersections(paths[p]).length;
	var gameCompletion = (circles.length*3)-(deadNodes.length*3);
	if (gameCompletion == intersections)
		return true;
	if (gameCompletion - 1 == intersections)
		return true;
	return false;
}

// Keeps all the path checks nice and organized without cluttering the onMouseUp function: 
function pathsChecker() {
    if (!pathEndPointsChecker())
        errorMsg.content = 'Illegal Move, Bad Path Start/End';
    else if (!pathCollisionChecker())
        errorMsg.content = 'Illegal Move, Path Collision';
    else if (!pathIntersectionsChecker())
        errorMsg.content = 'Illegal Move, Too many Circles Crossed';
    else
        return true;
    return false;
}

// Keeps all the circle checks nice and organized without cluttering the onMouseUp function: 
function circlesChecker() {
    if (!circleCollisionChecker())
        errorMsg.content = 'Illegal Move, Circle Collision';
    else if (!circleIntersectionsChecker())
        errorMsg.content = 'Illegal Move, Circle/Path Overlap';
    else if (!maxIntersectionChecker())
        errorMsg.content = 'Illegal Move, More than 3 Connections';
    else
        return true;
    return false;
}

// When the user finishes his path and lifts up from the mouseclick,
// the path goes through rigorous testing before being accepted and applied:
function onMouseUp(event) {
    if (turn < maxGame) {
		circleDone = 0;
        var p = paths.length - 1;
        var pathEnd = paths[p].segments.length;
        paths[p].simplify(10);
        if (pathsChecker()) {
            var offset = paths[p].length / 2;
            var posxy = paths[p].getPointAt(offset);
            circles.push( new Path.Circle({center: posxy, radius: r, fillColor: circleColor }) );
            workcircles.push( new Path.Circle({center: posxy, radius: r - 1 }) );
            if (!circlesChecker()){
                errorSoundFX.play();
                paths[p].removeSegments(0,pathEnd);
                paths.length = paths.length - 1;
                circles[circles.length - 1].removeSegments(0,5);
                circles.length = circles.length - 1;
            } else {
                finishedCircleColorer();
                turn = turn + 1;
                playerTurn();
                if (gameoverChecker()) {
                    if (sound)
                        gameEndSoundFX.play();
                    playerTurn1.content = '';
                    playerTurn2.content = '';
                    gameoverScreen.content = 'Game Over';
                    turn = turn + maxGame;
                }
            }
        } else {
            if (sound)
                errorSoundFX.play();
            paths[p].removeSegments(0,pathEnd);
            paths.length = paths.length - 1;
        }
    }
}

// Sets up the Player 1 turn and Player 2 turn text:
var playerTurn1 = new PointText({
    content: '',
    point: new Point (scale*10, scale*10),
    fillColor: 'black',
    fontSize: scale*6,
});
var playerTurn2 = new PointText({
    content: '',
    point: new Point (cx*2-scale*30, scale*10),
    fillColor: 'black',
    fontSize: scale*6,
});

// Sets up the error messages text: 
var errorMsg = new PointText({
    content: '',
    point: new Point(cx-40*scale,cy-scale*5),
    fillColor: 'red',
    fontSize: scale*6
});

// Sets up the gameover/restart/menu text:
var gameoverScreen = new PointText({
    content: '',
    point: new Point(mx, 75),
    fillColor: 'black',
    fontSize: 50,
});

// Start the game with user inputted amount of nodes
gameInit(nodes);

// Inline code for Sound On/Off slider: 
$("body").append($('<div style="position: fixed; right: 1em; bottom: 1em; color: black;">Sound<br><div class="switch"><label>Off<input type="checkbox" id="soundswitch"><span class="lever"></span>On</label></div></div>'));
document.getElementById("soundswitch").onchange = function() {sound = !sound};
document.getElementById("soundswitch").click();

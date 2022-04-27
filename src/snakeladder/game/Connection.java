// Modified by thurs 11 - team 9
package snakeladder.game;

import ch.aplu.jgamegrid.Location;

public abstract class Connection
{
  Location locStart;
  Location locEnd;
  int cellStart;
  int cellEnd;

  Connection(int cellStart, int cellEnd)
  {
    this.cellStart = cellStart;
    this.cellEnd = cellEnd;
    locStart = GamePane.cellToLocation(cellStart);
    locEnd = GamePane.cellToLocation(cellEnd);
  }

  void reverse() {
	  int temp_cell = cellStart;
	  Location temp_loc = locStart;
	  cellStart = cellEnd;
	  cellEnd = temp_cell;
	  locStart = locEnd;
	  locEnd = temp_loc;
  }
  
  String imagePath;
  
  public void setLocStart(Location loc_start) {
	  this.locStart = loc_start;
  }
  
  public void setLocEnd(Location loc_end) {
	  this.locEnd = loc_end;
  }

  public Location getLocStart() {
    return locStart;
  }

  public Location getLocEnd() {
    return locEnd;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public double xLocationPercent(int locationCell) {
    return (double) locationCell / GamePane.NUMBER_HORIZONTAL_CELLS;
  }
  public double yLocationPercent(int locationCell) {
    return (double) locationCell / GamePane.NUMBER_VERTICAL_CELLS;
  }
}

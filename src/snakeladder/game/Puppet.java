package snakeladder.game;

import ch.aplu.jgamegrid.*;
import java.awt.Point;

public class Puppet extends Actor
{
  private GamePane gamePane;
  private NavigationPane navigationPane;
  private int cellIndex = 0;
  private int nbSteps;
  private Connection currentCon = null;
  private int y;
  private int dy;
  private boolean isAuto;
  private String puppetName;
  private boolean minimumRoll;
  private Statistics playerStatistics;
  
  public Statistics getPlayerStatistics() {
	  return playerStatistics;
  }
   
  
  public void printPlayerStatistics() {
		System.out.print(puppetName + " rolled: " );		
		int k=navigationPane.getNumberDice();
		System.out.print(k + "-" + playerStatistics.getDieValuesCount().get(k));
		for (k++; k<=6*navigationPane.getNumberDice(); k++) {
			System.out.print(", " + k + "-" + playerStatistics.getDieValuesCount().get(k));
		}
		System.out.print("\n");
		System.out.print(puppetName + " traversed: ");
		System.out.print("up-" + playerStatistics.getConnectionsCount().get("up") + ", ");
		System.out.print("down-" + playerStatistics.getConnectionsCount().get("down") + "\n");
	}

  Puppet(GamePane gp, NavigationPane np, String puppetImage)
  {
    super(puppetImage);
    this.gamePane = gp;
    this.navigationPane = np;
    this.playerStatistics = new Statistics(navigationPane.getNumberDice());
  }

  public boolean isAuto() {
    return isAuto;
  }

  public void setAuto(boolean auto) {
    isAuto = auto;
  }

  public String getPuppetName() {
    return puppetName;
  }

  public void setPuppetName(String puppetName) {
    this.puppetName = puppetName;
  }

  void go(int nbSteps)
  {
	this.minimumRoll = (nbSteps == navigationPane.getNumberDice());
	
    if (cellIndex == 100)  // after game over
    {
      cellIndex = 0;
      setLocation(gamePane.startLocation);
    }
    
    this.nbSteps = nbSteps;
    setActEnabled(true);
  }

  void resetToStartingPoint() {
    cellIndex = 0;
    setLocation(gamePane.startLocation);
    setActEnabled(true);
  }

  int getCellIndex() {
    return cellIndex;
  }

  private void moveToNextCell()
  {
    int tens = cellIndex / 10;
    int ones = cellIndex - tens * 10;
    if (tens % 2 == 0)     // Cells starting left 01, 21, .. 81
    {
      if (ones == 0 && cellIndex > 0)
        setLocation(new Location(getX(), getY() - 1));
      else
        setLocation(new Location(getX() + 1, getY()));
    }
    else     // Cells starting left 20, 40, .. 100
    {
      if (ones == 0)
        setLocation(new Location(getX(), getY() - 1));
      else
        setLocation(new Location(getX() - 1, getY()));
    }
    cellIndex++;
  }
  
  void moveBackwardsCell()
  {
    int tens = cellIndex / 10;
    int ones = cellIndex % 10;
    // handle case if puppet is moved back from square 1
    if(cellIndex == 1)
    {
    	setLocation(gamePane.startLocation);
    }
    else if (tens % 2 == 0)     // Cells starting left 01, 21, .. 81
    {
      if (ones == 1 && cellIndex > 0)
        setLocation(new Location(getX(), getY() + 1));
      else
        setLocation(new Location(getX() - 1, getY()));
    }
    else     // Cells starting left 20, 40, .. 100
    {
      if (ones == 1)
        setLocation(new Location(getX(), getY() + 1));
      else
        setLocation(new Location(getX() + 1, getY()));
    }
    cellIndex--;
    nbSteps = 0;
    setActEnabled(true);
    
  }
  

  public void act()
  {
    if ((cellIndex / 10) % 2 == 0)
    {
      if (isHorzMirror())
        setHorzMirror(false);
    }
    else
    {
      if (!isHorzMirror())
        setHorzMirror(true);
    }
    
    

    // Animation: Move on connection
    if (currentCon != null)
    {
      int x = gamePane.x(y, currentCon);
      setPixelLocation(new Point(x, y));
      y += dy;

      // Check end of connection
      if ((dy > 0 && (y - gamePane.toPoint(currentCon.locEnd).y) > 0)
        || (dy < 0 && (y - gamePane.toPoint(currentCon.locEnd).y) < 0))
      {
        gamePane.setSimulationPeriod(100);
        setActEnabled(false);
        setLocation(currentCon.locEnd);
        cellIndex = currentCon.cellEnd;
        setLocationOffset(new Point(0, 0));
        currentCon = null;
        navigationPane.prepareTurn(cellIndex);
      }
      return;
    }

    // Normal movement
    if (nbSteps > 0)
    {
      moveToNextCell();

      if (cellIndex == 100)  // Game over
      {
        setActEnabled(false);
        navigationPane.prepareTurn(cellIndex);
        return;
      }

      nbSteps--;
      if (nbSteps == 0)
      {
    	// move puppets landed on backwards one cell
    	gamePane.movePuppetsLandedOnBack(cellIndex);
        
        // Check if on connection start
        if ((currentCon = gamePane.getConnectionAt(getLocation())) != null)
        {
          //check if we have minimum roll and connection takes us downwards
          if (minimumRoll && (currentCon.cellEnd < currentCon.cellStart)) {
        	  //ignore connection
        	  currentCon = null;
        	  setActEnabled(false);
              navigationPane.prepareTurn(cellIndex);
          }
          else {
        	  gamePane.setSimulationPeriod(50);
        	  y = gamePane.toPoint(currentCon.locStart).y;
        	  if (currentCon.locEnd.y > currentCon.locStart.y) {
        		  dy = gamePane.animationStep;
        		  gamePane.getPuppet().getPlayerStatistics().addConnection("down");
        	  }
        	  else {
        		  dy = -gamePane.animationStep;
        		  gamePane.getPuppet().getPlayerStatistics().addConnection("up");
        	  }
        	  if (currentCon instanceof Snake)
        	  {
        		  navigationPane.showStatus("Digesting...");
        		  navigationPane.playSound(GGSound.MMM);
        	  }
        	  else
        	  {
        		  navigationPane.showStatus("Climbing...");
        		  navigationPane.playSound(GGSound.BOING);
        	  }
          }
        }
        else
        {
          setActEnabled(false);
          navigationPane.prepareTurn(cellIndex);
        }
        
        
      }
    }
  }

}

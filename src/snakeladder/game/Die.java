// Modified by thurs 11 - team 9
package snakeladder.game;

import ch.aplu.jgamegrid.Actor;

public class Die extends Actor
{
  private NavigationPane np;
  private int nb;
  private boolean lastDie;

  Die(int nb, NavigationPane np, boolean lastDie)
  {
    super("sprites/pips" + nb + ".gif", 7);
    this.np = np;
    this.nb = nb;
    this.lastDie = lastDie;
  }

  public void act()
  {
    showNextSprite();
    if (getIdVisible() == 6)
    {
      setActEnabled(false);
      np.allowRolling(!lastDie);
      np.showPips("Pips: " + nb);
      if(lastDie) np.startMoving();
      else np.tryAutoRoll();
    }
  }

}

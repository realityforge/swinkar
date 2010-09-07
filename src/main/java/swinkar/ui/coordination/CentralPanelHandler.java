package swinkar.ui.coordination;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import swinkar.ui.menus.Menu;
import swinkar.ui.menus.MenuContainer;
import swinkar.ui.menus.MenuItem;
import swinkar.ui.menus.MenuItemPreparer;

@Component( immediate = true )
@Provides( specifications={ MenuItemPreparer.class } )
public class CentralPanelHandler
  implements MenuItemPreparer
{
  @Requires( proxy = false )
  private BackPanel _backPanel;

  public void prepareMenuItem( final MenuItem item, final MenuContainer context )
  {
    if ( "Say Oink!".equals( item.getLabelProperty() ))
    {
      item.setEnabled( !_backPanel.isOinking() );
    }
    if ( "Say Moo!".equals( item.getLabelProperty() ))
    {
      item.setEnabled( !_backPanel.isMooing() );
    }
  }
}

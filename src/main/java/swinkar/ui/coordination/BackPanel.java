package swinkar.ui.coordination;

import java.awt.BorderLayout;
import java.util.concurrent.Callable;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import swinkar.SwinkarUtil;

@Component( factory_method = "create", architecture = true, immediate = true, managedservice = "BackPanel" )
@Provides( specifications = { BackPanel.class, JPanel.class } )
public class BackPanel
  extends JPanel
{
  @ServiceProperty( name = "screenName", value = "Coordination" )
  private String m_screenName;

  @ServiceProperty( name = "role", value = "Screen" )
  private String m_role;

  private Boolean _isOink;

  public static BackPanel create()
  {
    return SwinkarUtil.invokeAndWait( new Callable<BackPanel>()
    {
      @Override
      public BackPanel call()
      {
        System.out.println( "Constructing a ScreenContainer" );
        final BackPanel screenContainer = new BackPanel();
        screenContainer.setLayout( new BorderLayout() );
        screenContainer.add( new JLabel( "left" ), BorderLayout.WEST );
        screenContainer.add( new JLabel( "right" ), BorderLayout.EAST );
        screenContainer.add( new JLabel( "middle" ), BorderLayout.CENTER );
        return screenContainer;
      }
    } );
  }

  public Boolean isOinking()
  {
    return Boolean.TRUE == _isOink;
  }

  public Boolean isMooing()
  {
    return Boolean.FALSE == _isOink;
  }

  public void oink()
  {
    _isOink = Boolean.TRUE;
    ( (JLabel) getComponent( 2 ) ).setText( "OINK!");
  }

  public void moo()
  {
    _isOink = Boolean.FALSE;
    ( (JLabel) getComponent( 2 ) ).setText( "MOO!");
  }
}

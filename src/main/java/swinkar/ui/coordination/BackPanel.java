package swinkar.ui.coordination;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.handlers.event.Subscriber;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.osgi.service.event.Event;
import org.realityforge.swung_weave.RunInEDT;

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

  private JLabel m_westLabel;
  private JLabel m_centerLabel;
  private JLabel m_eastLabel;

  private MouseListener m_mouseListener;

  @org.apache.felix.ipojo.handlers.event.Publisher( name = "backPanelPublisher", synchronous = false,
                                                    topics = "backPanel/popupTriggered" )
  private Publisher m_popupTriggerPublisher;

  protected BackPanel()
  {
    createComponents();
    layoutComponents();
    createListeners();
    installListeners();
  }

  @RunInEDT
  public static BackPanel create()
  {
    System.out.println( "Constructing a ScreenContainer" );
    return new BackPanel();
  }

  private void createComponents()
  {
    m_westLabel = new JLabel( "Left" );
    m_centerLabel = new JLabel( "Middle" );
    m_eastLabel = new JLabel( "Right" );
  }

  private void layoutComponents()
  {
    setLayout( new BorderLayout() );
    add( m_westLabel, BorderLayout.WEST );
    add( m_centerLabel, BorderLayout.CENTER );
    add( m_eastLabel, BorderLayout.EAST );
  }

  private void installListeners()
  {
    m_westLabel.addMouseListener( m_mouseListener );
    m_centerLabel.addMouseListener( m_mouseListener );
    m_eastLabel.addMouseListener( m_mouseListener );
  }

  private void createListeners()
  {
    m_mouseListener = new MouseAdapter()
    {

      @Override
      public void mouseClicked( final MouseEvent mouseEvent )
      {
        System.out.println( "Label Clicked" );
        if ( mouseEvent.isPopupTrigger() )
        {
          mayShowPopup( mouseEvent );
        }
      }

      @Override
      public void mousePressed( final MouseEvent mouseEvent )
      {
        mayShowPopup( mouseEvent );
      }

      @Override
      public void mouseReleased( final MouseEvent mouseEvent )
      {
        mayShowPopup( mouseEvent );
      }

      private void mayShowPopup( MouseEvent mouseEvent )
      {
        if ( mouseEvent.isPopupTrigger() )
        {
          System.out.println( "Popup Triggered" );
          final Properties data = new Properties();
          data.put( "mouseEvent", mouseEvent );
          data.put( "source", mouseEvent.getSource() );

          final JComponent source = (JComponent) mouseEvent.getSource();
          final String context = source instanceof JLabel ? ( (JLabel) source ).getText() : null;

          data.put( "context", context );

          String menuId;
          if ( source == m_centerLabel )
          {
            menuId = "BackPanelMiddleMenu";
          }
          else
          {
            menuId = "BackPanelSideMenu";
          }

          data.put( "menuId", menuId );
          m_popupTriggerPublisher.send( data );
        }
      }
    };


  }

  public Boolean isOinking()
  {
    return Boolean.TRUE == _isOink;
  }

  public Boolean isMooing()
  {
    return Boolean.FALSE == _isOink;
  }

  @Subscriber( name = "oinkPerformer",
               topics = "menuItem/actionPerformed",
               filter = "(command=oink)" )
  @RunInEDT
  public void oink( Event e )
  {
    _isOink = Boolean.TRUE;
    ( (JLabel) getComponent( 2 ) ).setText( "OINK!" );
  }

  @Subscriber( name = "mooPerformer",
               topics = "menuItem/actionPerformed",
               filter = "(command=moo)" )
  @RunInEDT
  public void moo( Event e )
  {
    _isOink = Boolean.FALSE;
    ( (JLabel) getComponent( 2 ) ).setText( "MOO!" );
  }
}

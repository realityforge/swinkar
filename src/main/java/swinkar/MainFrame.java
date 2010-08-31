package swinkar;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.Subscriber;

@Component( immediate = true, managedservice = "MainFrame" )
public class MainFrame extends JFrame
{
  private String m_title = "My Title";

  @Property( name = "width", value = "640" )
  private int m_width = 640;

  @Property( name = "height", value = "800" )
  private int m_height = 800;

  @Subscriber( name = "MainFrame.Title", topics = "MainFrame/Title", data_key = "title", data_type = "java.lang.String" )
  public void updateTitle( final String title )
  {
    m_title = title;
    setTitle( m_title );
  }

  @Validate
  public void start()
  {
    System.out.println( "MainFrame.start" );
    getContentPane().setPreferredSize( new Dimension( m_width, m_height ) );
    getContentPane().setSize( new Dimension( m_width, m_height ) );
    getContentPane().add( new JPanel() );
    pack();
    setTitle( m_title );
    setVisible( true );
  }

  @Invalidate
  public void stop()
  {
    System.out.println( "MainFrame.stop" );
    setVisible( false );
    dispose();
  }
}

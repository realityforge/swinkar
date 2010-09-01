package swinkar;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.Subscriber;

@Component( immediate = true, managedservice = "MainFrame" )
public class MainFrame extends JFrame
{
  @Property( name = "width", value = "640" )
  private int m_width = 640;

  @Property( name = "height", value = "800" )
  private int m_height = 800;

  @Requires( proxy = false )
  private JMenuBar m_menuBar;

  @Subscriber( name = "MainFrame.Title",
               topics = "MainFrame/Title",
               data_key = "title",
               data_type = "java.lang.String" )
  public void updateTitle( final String title )
  {
    Runnable runnable = new Runnable()
    {
      public void run()
      {
        setTitle( title );
      }
    };
    SwinkarUtil.invokeAndWait( runnable );
  }

  @Validate
  public void start()
    throws Exception
  {
    final Runnable runnable = new Runnable()
    {
      public void run()
      {
        setPreferredSize( new Dimension( m_width, m_height ) );
        setSize( new Dimension( m_width, m_height ) );
        setJMenuBar( m_menuBar );
        pack();
        setTitle( "My Initial Title" );
        MainFrame.this.repaint();
        setVisible( true );
      }
    };
    SwinkarUtil.invokeAndWait( runnable );
  }

  @Invalidate
  public void stop()
  {
    Runnable runnable = new Runnable()
    {
      public void run()
      {
        setVisible( false );
        dispose();
      }
    };
    SwinkarUtil.invokeAndWait( runnable );
  }
}

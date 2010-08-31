package swinkar;

import java.util.Properties;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;

@Component( immediate = true )
public class TitleChanger
{
  @org.apache.felix.ipojo.handlers.event.Publisher( name = "TitlePublisher", synchronous = false, topics = "MainFrame/Title", data_key = "title")
  private Publisher m_publisher;
  private Thread m_thread;
  private boolean m_stop;

  @Validate
  public void start()
  {
    final Runnable runnable = new Runnable()
    {
      public void run()
      {
        m_stop = false;
        while( !m_stop )
        {
          try
          {
            Thread.sleep( 1000 );
          }
          catch( InterruptedException e )
          {
          }
          send();
        }
      }
    };
    m_thread = new Thread( runnable);
    m_thread.start();
  }

  private void send()
  {
    final Properties data = new Properties();
    data.setProperty("title", "Time: " + System.currentTimeMillis() );
    m_publisher.send( data );
  }

  @Invalidate
  public void stop()
  {
    m_stop = true;
    try
    {
      m_thread.join( 1000 );
    }
    catch( InterruptedException e )
    {       
    }
  }
}

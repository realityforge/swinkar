package swinkar;

import java.util.Dictionary;
import java.util.concurrent.Callable;
import javax.swing.RepaintManager;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Updated;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.handlers.jmx.Config;

@Component( immediate = true, managedservice = "EDT_Verifier", architecture = true, factory_method = "create" )
@Config( domain = "swinkar" )
public class EdtVerifier
{
  private CheckThreadViolationRepaintManager m_repaintManager = new CheckThreadViolationRepaintManager();

  @org.apache.felix.ipojo.handlers.jmx.Property( name = "fullCheck", rights = "w" )
  @Property( name = "fullCheck", value = "true" )
  private boolean m_fullCheck = true;

  @org.apache.felix.ipojo.handlers.jmx.Property( name = "active", rights = "w" )
  @Property( name = "active", value = "true" )
  private boolean m_active = true;

  public static EdtVerifier create()
  {
    return SwinkarUtil.invokeAndWait( new Callable<EdtVerifier>()
    {
      @Override
      public EdtVerifier call() throws Exception
      {
        return new EdtVerifier();
      }
    } );
  }

  @Validate
  public void start() throws Exception
  {
    System.out.println( "EdtVerifier.start" );
    updateRepaintManager( m_active );
  }

  @Invalidate
  public void stop() throws Exception
  {
    System.out.println( "EdtVerifier.stop" );
    if( m_active )
    {
      updateRepaintManager( false );
    }
  }

  public final boolean isFullCheck()
  {
    return m_fullCheck;
  }

  public final void setFullCheck( final boolean fullCheck )
  {
    m_fullCheck = fullCheck;
    m_repaintManager.setFullCheck( m_fullCheck );
  }

  @Updated
  public void configUpdated( final Dictionary config )
  {
    updateRepaintManager( m_active );
  }

  private void updateRepaintManager( final boolean active )
  {
    if( active )
    {
      setFullCheck( m_fullCheck );
      RepaintManager.setCurrentManager( m_repaintManager );
    }
    else
    {
      if( m_repaintManager == RepaintManager.currentManager( null ) )
      {
        RepaintManager.setCurrentManager( null );
      }
    }
  }
}
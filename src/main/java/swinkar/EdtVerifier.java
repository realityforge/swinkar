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
import org.realityforge.swung_weave.DispatchUtil;
import org.realityforge.swung_weave.EDTViolation;
import org.realityforge.swung_weave.EDTViolationListener;
import org.realityforge.swung_weave.RunInEDT;

@Component( immediate = true, managedservice = "EDT_Verifier", architecture = true, factory_method = "create" )
@Config( domain = "swinkar" )
public class EdtVerifier
  implements EDTViolationListener
{
  @org.apache.felix.ipojo.handlers.jmx.Property( name = "fullCheck", rights = "w" )
  @Property( name = "fullCheck", value = "true" )
  private boolean m_fullCheck = true;

  @org.apache.felix.ipojo.handlers.jmx.Property( name = "active", rights = "w" )
  @Property( name = "active", value = "true" )
  private boolean m_active = true;

  @RunInEDT
  public static EdtVerifier create()
  {
    return new EdtVerifier();
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
    System.out.println( "EdtVerifier.isFullCheck" );
    return m_fullCheck;
  }

  public final void setFullCheck( final boolean fullCheck )
  {
    System.out.println( "EdtVerifier.setFullCheck" );
    m_fullCheck = fullCheck;
    updateRepaintManager( m_active );
  }

  @Updated
  public void configUpdated( final Dictionary config )
  {
    updateRepaintManager( m_active );
  }

  private void updateRepaintManager( final boolean active )
  {
    if ( active )
    {
      DispatchUtil.installEDTViolationListener( this, m_fullCheck );
    }
    else
    {
      DispatchUtil.uninstallEDTViolationListener();
    }
  }

  @Override
  public void violationOccurred( final EDTViolation violation )
  {
    System.err.print( violation.toString() );
  }
}
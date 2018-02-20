package ets.wms13.core.dgiwg.testsuite.getcapabilities;

import static de.latlon.ets.core.assertion.ETSAssert.assertXPath;

//import javax.xml.soap.SOAPException;

import org.testng.SkipException;
import org.testng.annotations.Test;

import de.latlon.ets.core.assertion.ETSAssert;

import de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;

/**
 * Tests if the capabilities provides all mandatory and optional service metadata elements.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 * @author Jim Beatty (modified/fixed 06-June-2017)
 */
public class GetCapabilitiesContentTest extends AbstractBaseGetCapabilitiesFixture 
{
    // --- Mandatory Metadata --------
	
    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.6.2.2., S.13, Requirement 9")
    public void wmsCapabilitiesNameExists()
    //                throws SOAPException
    {
        String xPathXml = "//wms:WMS_Capabilities/wms:Service/wms:Name/text() != ''";
        assertXPath( xPathXml, wmsCapabilities, NS_BINDINGS );
    }

    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.6.2.2., S.13, Requirement 9")
    public void wmsCapabilitiesTitleExists()
    //                throws SOAPException 
    {
        String xPathXml = "//wms:WMS_Capabilities/wms:Service/wms:Title/text() != ''";
        assertXPath( xPathXml, wmsCapabilities, NS_BINDINGS );
    }

    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.6.2.2., S.13, Requirement 9")
    public void wmsCapabilitiesOnlineResourceExists()
    //                throws SOAPException 
    {
        String xPathXml = "//wms:WMS_Capabilities/wms:Service/wms:OnlineResource/@xlink:href != ''";
        assertXPath( xPathXml, wmsCapabilities, NS_BINDINGS );
    }

    // --- Optional Metadata --------
     
    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.6.2.2., S.13, Requirement 9")
    public void wmsCapabilitiesAbstractExists()
      //              throws SOAPException 
    {
        String xPathXml = "//wms:WMS_Capabilities/wms:Service/wms:Abstract";
    	skipIfNoOptionalMetadata( xPathXml );
    	
    	xPathXml += "/text() != ''";
    	skipIfNoOptionalMetadata( xPathXml );
    	
        assertXPath( xPathXml, wmsCapabilities, NS_BINDINGS );
    }

    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.6.2.2., S.13, Requirement 9")
    public void wmsCapabilitiesKeywordListExists()
        //            throws SOAPException
    {
        String xPathXml = "//wms:WMS_Capabilities/wms:Service/wms:KeywordList";
    	skipIfNoOptionalMetadata( xPathXml );
    	
    	xPathXml += "/text() != ''";
    	skipIfNoOptionalMetadata( xPathXml );
    	
        assertXPath( xPathXml, wmsCapabilities, NS_BINDINGS );
    }

    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.6.2.2., S.13, Requirement 9")
    public void wmsCapabilitiesContactInformationExists()
          //          throws SOAPException 
    {
        String xPathXml = "//wms:WMS_Capabilities/wms:Service/wms:ContactInformation";
    	skipIfNoOptionalMetadata( xPathXml );
    	
    	xPathXml += "/text() != ''";
    	skipIfNoOptionalMetadata( xPathXml );
    	
        assertXPath( xPathXml, wmsCapabilities, NS_BINDINGS );
    }

    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.6.2.2., S.13, Requirement 9")
    public void wmsCapabilitiesFeesExists()
            //        throws SOAPException
    {
        String xPathXml = "//wms:WMS_Capabilities/wms:Service/wms:Fees";
    	skipIfNoOptionalMetadata( xPathXml );
    	
    	xPathXml += "/text() != ''";
    	skipIfNoOptionalMetadata( xPathXml );
    	
        assertXPath( xPathXml, wmsCapabilities, NS_BINDINGS );
    }

    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.6.2.2., S.13, Requirement 9")
    public void wmsCapabilitiesAccessConstraintsExists()
              //      throws SOAPException
    {
    	String xPathXml = "//wms:WMS_Capabilities/wms:Service/wms:AccessConstraints";
    	skipIfNoOptionalMetadata( xPathXml );
    	
    	xPathXml += "/text() != ''";
    	skipIfNoOptionalMetadata( xPathXml );
    	
        assertXPath( xPathXml, wmsCapabilities, NS_BINDINGS );
    }

    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.6.2.2., S.13, Requirement 9")
    public void wmsCapabilitiesLayerLimitExists()
         //           throws SOAPException
    {
    	String xPathXml = "//wms:WMS_Capabilities/wms:Service/wms:LayerLimit";
    	skipIfNoOptionalMetadata( xPathXml );
    	
    	xPathXml += "/text() != ''";
    	skipIfNoOptionalMetadata( xPathXml );
    	
        assertXPath( xPathXml, wmsCapabilities, NS_BINDINGS );
    }

    // --- --------
    
    protected void skipIfNoOptionalMetadata( String xPath )
    {
    	boolean metadataEvaluates = ETSAssert.checkXPath( xPath, wmsCapabilities, NS_BINDINGS );
        if ( !metadataEvaluates )
        {
        	if ( xPath.endsWith("''") )
        		throw new SkipException("The WMS contains the optional capabilities metadata but is blank, tests are skipped!" );
        	else
        		throw new SkipException( "The WMS does not contain optional capabilities metadata, tests are skipped!" );
        }
    }
    
    // ---
    /*  
    private XPath createXPath()
            throws XPathFactoryConfigurationException 
    {
    	XPathFactory factory = XPathFactory.newInstance( XPathConstants.DOM_OBJECT_MODEL );
    	XPath xpath = factory.newXPath();
    	xpath.setNamespaceContext( NS_BINDINGS );
    	return xpath;
    }
    */
    
}
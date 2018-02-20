package ets.wms13.core.dgiwg.testsuite.getcapabilities;

import static de.latlon.ets.core.assertion.ETSAssert.assertUriIsResolvable;
import static de.latlon.ets.core.assertion.ETSAssert.assertUrl;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import de.latlon.ets.core.assertion.ETSAssert;
import de.latlon.ets.wms13.core.dgiwg.testsuite.getcapabilities.AbstractBaseGetCapabilitiesFixture;


/**
 * Tests if the FeatureListUrls are resolvable.
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 */
public class GetCapabilitiesFeatureListUrlTest extends AbstractBaseGetCapabilitiesFixture {

    @DataProvider(name = "featureListUrls")
    public Object[][] parseFeatureListUrlNodes( ITestContext testContext )
                    throws XPathFactoryConfigurationException, XPathExpressionException
    {
        if ( this.wmsCapabilities == null )
            initBaseFixture( testContext );
        NodeList featureListUrlNodes = parseFeatureListURLNodes( wmsCapabilities );
        Object[][] featureListUrls = new Object[featureListUrlNodes.getLength()][];
        for ( int featureListUrlNodeIndex = 0; featureListUrlNodeIndex < featureListUrlNodes.getLength(); featureListUrlNodeIndex++ ) {
            Node featureListUrlNode = featureListUrlNodes.item( featureListUrlNodeIndex );
            String featureListUrl = (String) createXPath().evaluate( "//@xlink:href", featureListUrlNode, XPathConstants.STRING );
            featureListUrls[featureListUrlNodeIndex] = new Object[] { featureListUrl };
        }
        
        if ( featureListUrlNodes.getLength() <= 0 )
        {
        	throw new SkipException("There are no FeatureListURLs; tests skipped");
        }
        
        return featureListUrls;
    }

    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.6.2.3., S.17, Requirement 20", dataProvider = "featureListUrls")
    public void wmsCapabilitiesFeatureListURLIsResolvable( String featureListUrl )
                                    throws XPathExpressionException, XPathFactoryConfigurationException 
    {
        while (featureListUrl.contains(" "))
        {
        	featureListUrl = featureListUrl.substring(0, featureListUrl.indexOf(" ")) + "%20" + featureListUrl.substring(featureListUrl.indexOf(" ")+1);
        }
        assertUrl( featureListUrl );
        assertUriIsResolvable( featureListUrl );
    }

   // --- --------
     
    private NodeList parseFeatureListURLNodes( Document entity )
                    throws XPathFactoryConfigurationException, XPathExpressionException {
        String xPathFeatureListUrls = "//wms:Layer/wms:FeatureListURL/wms:OnlineResource";
        XPath xpath = createXPath();
        return (NodeList) xpath.evaluate( xPathFeatureListUrls, entity, XPathConstants.NODESET );
    }

    // ---
    
    private XPath createXPath()
                    throws XPathFactoryConfigurationException {
        XPathFactory factory = XPathFactory.newInstance( XPathConstants.DOM_OBJECT_MODEL );
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext( NS_BINDINGS );
        return xpath;
    }

}
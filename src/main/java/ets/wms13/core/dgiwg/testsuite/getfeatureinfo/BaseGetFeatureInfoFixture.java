package ets.wms13.core.dgiwg.testsuite.getfeatureinfo;

import static de.latlon.ets.wms13.core.domain.DGIWGWMS.GET_FEATURE_INFO;
import static de.latlon.ets.wms13.core.domain.DGIWGWMS.TEXT_XML;

//import java.util.Iterator;
import java.util.List;

//import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.testng.annotations.BeforeClass;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
//import org.w3c.dom.Node;

import de.latlon.ets.wms13.core.client.WmsKvpRequest;
import de.latlon.ets.wms13.core.dgiwg.testsuite.AbstractBaseGetFixture;
//import de.latlon.ets.wms13.core.domain.DGIWGWMS;
import de.latlon.ets.wms13.core.util.ServiceMetadataUtils;
import de.latlon.ets.wms13.core.util.request.WmsRequestBuilder;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 * @author Jim Beatty (modified/fixed 06-June-2017), by extension using lower-level fix
 */
public class BaseGetFeatureInfoFixture extends AbstractBaseGetFixture {

    /**
     * Builds a {@link WmsKvpRequest} representing a GetMap request.
     */
    @BeforeClass
    public void buildGetMapRequest()
    {
    	List<String> formats = ServiceMetadataUtils.parseSupportedFormats( wmsCapabilities, GET_FEATURE_INFO );
    	if (formats.contains(TEXT_XML))
    	{
            this.reqEntity = WmsRequestBuilder.buildGetFeatureInfoRequest( wmsCapabilities, layerInfo, TEXT_XML );
    	}
    	else
    	{
    		this.reqEntity = WmsRequestBuilder.buildGetFeatureInfoRequest( wmsCapabilities, layerInfo );//, TEXT_XML );
    	}
    }

    protected NodeList parseFeatureMemberNodes( Document entity )
                    throws XPathFactoryConfigurationException, XPathExpressionException 
    {
        String xPathAbstract = "//*[local-name() = 'FeatureInfoResponse']/*[local-name() = 'FIELDS']";
        return (NodeList) createXPath().evaluate( xPathAbstract, entity, XPathConstants.NODESET );
    }

    protected XPath createXPath()
                    throws XPathFactoryConfigurationException {
        XPathFactory factory = XPathFactory.newInstance( XPathConstants.DOM_OBJECT_MODEL );
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext( NS_BINDINGS );
        return xpath;
    }
    
    // --- --------
    
 
}
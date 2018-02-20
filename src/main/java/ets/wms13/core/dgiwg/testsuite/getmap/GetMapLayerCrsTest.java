package ets.wms13.core.dgiwg.testsuite.getmap;

import static de.latlon.ets.core.assertion.ETSAssert.assertContentType;
import static de.latlon.ets.core.assertion.ETSAssert.assertStatusCode;
import static de.latlon.ets.wms13.core.domain.DGIWGWMS.BBOX_PARAM;
import static de.latlon.ets.wms13.core.domain.DGIWGWMS.CRS_PARAM;
import static de.latlon.ets.wms13.core.domain.DGIWGWMS.FORMAT_PARAM;
import static de.latlon.ets.wms13.core.domain.DGIWGWMS.GET_MAP;
import static de.latlon.ets.wms13.core.domain.DGIWGWMS.IMAGE_PNG;
import static de.latlon.ets.wms13.core.domain.DGIWGWMS.LAYERS_PARAM;
import static de.latlon.ets.wms13.core.domain.ProtocolBinding.GET;
import static de.latlon.ets.wms13.core.util.ServiceMetadataUtils.getOperationEndpoint;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.ClientResponse;

import de.latlon.ets.core.error.ErrorMessage;
import de.latlon.ets.core.error.ErrorMessageKey;
import de.latlon.ets.wms13.core.crs.CrsMatcher;
import de.latlon.ets.wms13.core.domain.BoundingBox;
import de.latlon.ets.wms13.core.domain.LayerInfo;

/**
 * Tests if the Layer is requestable with the required CRS.
 * 
 * @author <a href="mailto:stenger@lat-lon.de">Dirk Stenger</a>
 * @author Jim Beatty (modified/fixed 24-May-2017)
 */
public class GetMapLayerCrsTest extends BaseGetMapFixture {

    private static final CrsMatcher CRS_MATCHER = new CrsMatcher();

    private static final String REQUEST_FORMAT = IMAGE_PNG;

    @DataProvider(name = "layerNodes")
    public Object[][] parseLayerNodes( ITestContext testContext )
                    throws XPathFactoryConfigurationException, XPathExpressionException {
        Object[][] layers = new Object[layerInfo.size()][];
        for ( int layerIndex = 0; layerIndex < layerInfo.size(); layerIndex++ ) {
        	LayerInfo layer = layerInfo.get( layerIndex );
        	layers[layerIndex] = new Object[] { layer };
        }
        
        if ( layerInfo.size() <= 0 )
        {
        	throw new SkipException("There are no Layers; tests skipped");
        }
        
        return layers;
    }

    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.5.4.2., S.11, Requirement 6", dataProvider = "layerNodes")
    public void wmsGetMapLayerCrs_Mandatory_CRS_84_Supported( LayerInfo layer ) 
    {
        String crs = "CRS:84";
        ClientResponse rsp = sendGetMapCrsRequest (layer, crs);

        checkGetMapResponse(rsp, "Mandatory_CRS_84_Supported_By_Layer_" + layer.getLayerName() );
    }

    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.5.4.2., S.11, Requirement 6", dataProvider = "layerNodes")
    public void wmsGetMapLayerCrs_Mandatory_EPSG_4326_Supported( LayerInfo layer ) 
    {
        String crs = "EPSG:4326";
        ClientResponse rsp = sendGetMapCrsRequest (layer, crs);

        checkGetMapResponse(rsp, "Mandatory_EPSG_4326_Supported_By_Layer_" + layer.getLayerName() );
    }

    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.5.4.2., S.11, Requirement 6.1", dataProvider = "layerNodes")
    public void wmsGetMapLayerCrs_Mandatory_EPSG_3395_Supported( LayerInfo layer ) 
    {
        String crs = "EPSG:3395";
        ClientResponse rsp = sendGetMapCrsRequest (layer, crs);

        checkGetMapResponse(rsp, "Mandatory_EPSG_3395_Supported_By_Layer_" + layer.getLayerName() );
    }

    @Test(description = "DGIWG - Web Map Service 1.3 Profile, 6.5.4.2., S.11, Requirement 6.1", dataProvider = "layerNodes")
    public void wmsGetMapLayerCrs_Conditional_Supported( LayerInfo layer )
    {
        List<String> conditionalExpectedCrs = CRS_MATCHER.retrieveOverlappingCrs( layer.getGeographicBbox() );

        for ( String crs : conditionalExpectedCrs ) 
        {
            ClientResponse rsp = sendGetMapCrsRequest (layer, crs);

            checkGetMapResponse(rsp, "Conditional_" + crs.replace( ":", "_" ) + "_Supported_By_Layer_" + layer.getLayerName() );
        }
    }
    
    
    public void checkGetMapCrs( LayerInfo layer, String crs )
    {
    	ClientResponse rsp = sendGetMapCrsRequest ( layer, crs );
    	checkGetMapResponse( rsp, "Confirmed__" + crs.replace( ":", "_" ) + "_Supported_By_Layer_" + layer.getLayerName() );
    }

    // ---
    
    private String findBboxOrSkipTestIfCrsIsNotSupported( LayerInfo layer, String crs ) {
        for ( BoundingBox bbox : layer.getBboxes() ) {
            if ( crs.equals( bbox.getCrs() ) )
                return bbox.getBboxAsString();
        }
//        throw new SkipException( "Layer " + layer.getLayerName() + " does not support CRS " + crs + ", tests are skipped!" );
        assertTrue(false,  "Layer " + layer.getLayerName() + " does not support CRS " + crs);
        return null;
    }

    // ---
    
    private ClientResponse sendGetMapCrsRequest ( LayerInfo layer, String crs )
    {
        String bbox = findBboxOrSkipTestIfCrsIsNotSupported( layer, crs );
        String layerName = layer.getLayerName();

        this.reqEntity.addKvp( LAYERS_PARAM, layerName );
        this.reqEntity.addKvp( CRS_PARAM, crs );
        this.reqEntity.addKvp( BBOX_PARAM, bbox );
        this.reqEntity.addKvp( FORMAT_PARAM, REQUEST_FORMAT );

        URI endpoint = getOperationEndpoint( this.wmsCapabilities, GET_MAP, GET );
        ClientResponse rsp = null;
        rsp = wmsClient.submitRequest( this.reqEntity, endpoint );
    
        return rsp;
    }
    
    // ---

    private void checkGetMapResponse(ClientResponse rsp, String message)
    {
    	storeResponseImage( rsp, "Requirement6", message, REQUEST_FORMAT );

        assertTrue( rsp.hasEntity(), ErrorMessage.get( ErrorMessageKey.MISSING_XML_ENTITY ) );
        assertStatusCode( rsp.getStatus(), 200 );
        assertContentType( rsp.getHeaders(), REQUEST_FORMAT );
    }
}
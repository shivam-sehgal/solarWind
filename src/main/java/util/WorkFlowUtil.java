package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import model.WorkFlowTemplate;

public class WorkFlowUtil {

  private static ObjectMapper objectMapper = new ObjectMapper();


  public static WorkFlowTemplate loadTemplateFromTenant(String tenenantId)  {
    Path tenantPath = Paths.get("workflows/"+tenenantId);
    Path defaultPath = Paths.get("workflows/default");
    Path finalPath = Files.exists(tenantPath)?tenantPath:defaultPath;
    try(InputStream in = Files.newInputStream(finalPath)){
      WorkFlowTemplate[] templates = objectMapper.readValue(in, WorkFlowTemplate[].class);

      return templates[0];

    }catch (IOException e){
      throw  new RuntimeException("Faliled to load workflow Json tenanat"  + finalPath);
    }






  }


}

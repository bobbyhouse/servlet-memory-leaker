

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

/**
 * Servlet implementation class MemoryLeaker
 */
@WebServlet("/MemoryLeaker")
public class MemoryLeaker extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String leaky_message;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemoryLeaker() {
        super();
        this.leaky_message = "No message yet. Try POST-ing something. For example:" +
        		"curl -X POST http://localhost:8080/memory-leaker/MemoryLeaker " +
        		"-d '{message: my favorite messages usually have something to do with tacos}'";
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject response_object;
		response.setContentType("application/json");
		try {
			PrintWriter out = response.getWriter();
			response_object = new JSONObject().put("message", leaky_message);
			out.print(response_object.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Read in the JSON data from the input string
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
		  } catch (Exception e) {
			  e.printStackTrace();
		  }

		// Turn the string into a JSON object and update leaky_message
		// interning a string will put it in memory that will never be garbage collected
		try {
			JSONObject jsonObject = new JSONObject(jb.toString());
		    leaky_message = jsonObject.getString("message");
		    leaky_message.intern();
		  } catch (ParseException e) {
			e.printStackTrace();
		  } catch (JSONException e) {
			e.printStackTrace();
		}
		
		// Return a response with the current memory usage
		JSONObject response_object;
	    Runtime runtime = Runtime.getRuntime();
		response.setContentType("application/json");
		try {
			PrintWriter out = response.getWriter();
			response_object = new JSONObject();
			response_object.put("total_memory", runtime.totalMemory());
		    response_object.put("max_memory", runtime.maxMemory());
		    response_object.put("free_memory", runtime.freeMemory());
			out.print(response_object.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		} 
	}
}

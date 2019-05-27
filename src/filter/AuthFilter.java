//package filter;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//
//import com.RapidFeedback.InsideFunction;
//import com.RapidFeedback.MysqlFunction;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//
///**
// * Servlet Filter implementation class AuthFilter
// */
////@WebFilter("/AuthFilter")
//@WebFilter(filterName = "authFilter", servletNames= {"",""})
//public class AuthFilter implements Filter {
//	
//	FilterConfig config;
//	
//	public void setFilterConfig(FilterConfig config) {
//	    this.config = config;
//	}
//
//	public FilterConfig getFilterConfig() {
//	    return config;
//	}
//
//    /**
//     * Default constructor. 
//     */
//    public AuthFilter() {
//        // TODO Auto-generated constructor stub
//    }
//
//	/**
//	 * @see Filter#destroy()
//	 */
//	public void destroy() {
//		// TODO Auto-generated method stub
//	}
//
//	/**
//	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
//	 */
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		// TODO Auto-generated method stub
//		//get JSONObject from request
//		MysqlFunction dbFunction = new MysqlFunction();
//		InsideFunction inside = new InsideFunction(dbFunction);
//		JSONObject jsonReceive;
//		BufferedReader reader = request.getReader();
//		String str, wholeString = "";
//		while((str = reader.readLine()) != null)
//		{
//		    wholeString += str;  
//		}
//		System.out.println("Receive: " + wholeString);
//		jsonReceive = JSON.parseObject(wholeString);
//			
//			//get values from received JSONObject
//		String token = jsonReceive.getString("token");
//		ServletContext servletContext = this.getFilterConfig().getServletContext();
//		String username = inside.token2user(servletContext, token); 
//
//		// pass the request along the filter chain
//		chain.doFilter(request, response);
//	}
//
//	/**
//	 * @see Filter#init(FilterConfig)
//	 */
//	public void init(FilterConfig fConfig) throws ServletException {
//		// TODO Auto-generated method stub
//	}
//
//}

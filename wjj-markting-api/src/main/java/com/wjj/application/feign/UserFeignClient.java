/**  
* <p>Title: UserFeignClient.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年7月17日  
* @version 1.0  
*/  
package com.wjj.application.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.plugins.Page;
import com.wjj.application.entity.User;
import com.wjj.application.vo.PageVO;
import com.wjj.application.vo.Participant;
import com.wjj.application.vo.UserRequest;
import com.wjj.application.vo.UserTestVO;

/**  
* <p>Title: UserFeignClient.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年7月17日  
* @version 1.0  
*/
@FeignClient("wjj-user")
public interface UserFeignClient {
	
	@PostMapping("/page/list")
	Page<UserTestVO> userList(@RequestBody PageVO vo);
	
	@GetMapping(value = "/user/view")
	User userView(@RequestParam("id") Integer id);

	@PostMapping(value = "/user/update")
	User userUpdate(@RequestParam("id") Integer id);

	/**  
	 * <p>Title: reserve</p>  
	 * <p>Description: </p>  
	 * @param userRequest
	 * @return  
	 * @author bob
	 */
	@PostMapping(value = "/user/trying")
	Participant reserve(@RequestBody UserRequest userRequest);

}

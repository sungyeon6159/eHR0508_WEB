package com.sist.ehr.code;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.sist.ehr.board.service.BoardVO;
import com.sist.ehr.board.service.imple.BoardDaoImpl;
import com.sist.ehr.cmn.DTO;
import com.sist.ehr.cmn.SearchVO;
import com.sist.ehr.code.service.CodeDao;
import com.sist.ehr.code.service.CodeVO;
import com.sist.ehr.code.service.imple.CodeDaoImpl;
import com.sist.ehr.member.service.Level;

import com.sist.ehr.member.service.imple.UserDaoImple;



@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		                           "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
                                   })
public class TestCodeDao {

	private final Logger  LOG = LoggerFactory.getLogger(TestCodeDao.class);
	
	@Autowired
	WebApplicationContext  webApplicationContext;

	CodeVO code01;
	
	@Autowired
	CodeDaoImpl  dao;
	
	@Test
	public void doRetrieve() {
		//검색조건
		code01.setCodeTypeId("BOARD_SEARCH");
		List<CodeVO> list = (List<CodeVO>) dao.doRetrieve(code01);
		
		for(CodeVO vo:list) {
			LOG.debug("vo:"+vo.toString());
		}
		
		assertThat(list.size(), is(3));
	}
	
	
	@Before
	public void setUp() throws Exception {
		LOG.debug("^^^^^^^^^^^");
		LOG.debug("^WebApplicationContext^"+webApplicationContext);
		LOG.debug("^^^^^^^^^^^");
		code01=new CodeVO();
		
	}

	@After
	public void tearDown() throws Exception {
		LOG.debug("^^^^^^^^^^^");
		LOG.debug("^tearDown^");
		LOG.debug("^^^^^^^^^^^");		
	}
	

	 
	@Test
	@Ignore
	public void test() {
		LOG.debug("=====================");
		LOG.debug("=test()=");
		LOG.debug("=====================");
		
		
		LOG.debug("=====================");
		LOG.debug("=dao()="+dao);
		LOG.debug("=====================");		
		
		assertNotNull(dao);
		assertThat(1, is(1));
		
	}
	
	
	
	

}

package com.sist.ehr.board.web;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.sist.ehr.board.service.BoardService;
import com.sist.ehr.board.service.BoardVO;
import com.sist.ehr.cmn.DTO;
import com.sist.ehr.cmn.MessageVO;
import com.sist.ehr.cmn.SearchVO;
import com.sist.ehr.cmn.StringUtil;
import com.sist.ehr.code.service.CodeService;
import com.sist.ehr.code.service.CodeVO;

@Controller
public class BoardController {

	Logger  LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	CodeService codeService;
	
	
	@Autowired
	MessageSource messageSource;
	
	@RequestMapping(value = "board/do_retrieve.do",method = RequestMethod.GET)
	public String doRetrieve(SearchVO  search,Model model) {
		//param 기본값 처리
		if(search.getPageNum()==0) {
			search.setPageNum(1);;
		}
		
		if(search.getPageSize()==0) {
			search.setPageSize(10);
		}
		
		
		search.setSearchDiv(StringUtil.nvl(search.getSearchDiv()));
		search.setSearchWord(StringUtil.nvl(search.getSearchWord().trim()));
		
		LOG.debug("1=================");
		LOG.debug("1=param="+search);
		LOG.debug("1=================");
		//검색조건 화면으로 전달.
		model.addAttribute("vo", search);
		//TODO: codeTable : 검색조건,페이지 사이즈
		//검색조건
		CodeVO  code=new CodeVO();
		code.setCodeTypeId("BOARD_SEARCH");
		List<CodeVO> searchList=(List<CodeVO>) this.codeService.doRetrieve(code);
		LOG.debug("1.1=searchList="+searchList);
		model.addAttribute("searchList", searchList);
		
		//페이지 사이즈: PAGE_SIZE
		code.setCodeTypeId("PAGE_SIZE");
		List<CodeVO> pageSizeList=(List<CodeVO>) this.codeService.doRetrieve(code);
		LOG.debug("1.1=pageSizeList="+pageSizeList);
		model.addAttribute("pageSizeList", pageSizeList);
		
		
		List<BoardVO> list = (List<BoardVO>) this.boardService.doRetrieve(search);
		//조회결과 화면 전달
		model.addAttribute("list", list);
		for(BoardVO vo:list) {
			LOG.debug("1.1=out="+vo);
		}
		
		//총건수
		int totalCnt = 0;
		if(null !=list &&  list.size()>0) {
			totalCnt = list.get(0).getTotalCnt();
		}
		
		LOG.debug("1.2=================");
		LOG.debug("1.2=totalCnt="+totalCnt);
		LOG.debug("1.2=================");
		//조회결과 화면 전달
		model.addAttribute("totalCnt", totalCnt);
		return "board/board_list";// "/board/board_list.jsp
	}
	
	@RequestMapping(value = "board/do_update.do",method = RequestMethod.POST
			,produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String doUpdate(BoardVO  board,Locale locale) {
		LOG.debug("1=================");
		LOG.debug("1=param="+board);
		LOG.debug("1=================");
				
		if( 0 == board.getBoardId()) {		

			throw new IllegalArgumentException("Board_ID를 확인 하세요.");			
		}
		
		if(null == board.getTitle() || "".equals(board.getTitle().trim())) {
			//다국어 메시지 처리
			String title = messageSource.getMessage("message.board.search.condition.title"
					 , null, locale);
			Object[] args = new String[]{title};			
			String commMsg = messageSource.getMessage("message.common.message.save", args, locale);
			LOG.debug("1.1=================");
			LOG.debug("1.1=commMsg="+commMsg);
			LOG.debug("1.1=================");			

			throw new IllegalArgumentException(commMsg);
		}
		
		if(null == board.getContents() || "".equals(board.getContents().trim())) {
			//다국어 메시지 처리
			String contents = messageSource.getMessage("message.board.search.condition.content"
					, null, locale);
			Object[] args = new String[]{contents};			
			String commMsg = messageSource.getMessage("message.common.message.save", args, locale);
			LOG.debug("1.1=================");
			LOG.debug("1.1=commMsg="+commMsg);
			LOG.debug("1.1=================");	
			
			throw new IllegalArgumentException(commMsg);
		}		
		
		int flag = this.boardService.doUpdate(board);
		
		MessageVO message=new MessageVO();
		
		if(flag>0) {
			message.setMsgId(String.valueOf(flag));
			message.setMsgMsg("수정 성공.");
		}else {
			message.setMsgId(String.valueOf(flag));
			message.setMsgMsg("수정 실패.");			
		}
		
		Gson gson=new Gson();
		String jsonStr = gson.toJson(message);
		LOG.debug("1.2=================");
		LOG.debug("1.2=jsonStr="+jsonStr);
		LOG.debug("1.2=================");		
		return jsonStr;
	}

	
	@RequestMapping(value = "board/do_delete.do",method = RequestMethod.POST
			,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doDelete(BoardVO  board,Locale locale) {
		//param board_id
		LOG.debug("1=================");
		LOG.debug("1=param="+board);
		LOG.debug("1=================");
		
		if( 0 == board.getBoardId()) {		

			throw new IllegalArgumentException("Board_ID를 확인 하세요.");			
		}		
		
		int flag = this.boardService.doDelete(board);
		MessageVO message=new MessageVO();
		//삭제 성공
		if(flag>0) {
			message.setMsgId(String.valueOf(flag));
			message.setMsgMsg("삭제 되었습니다.");
		//실패	
		}else {
			message.setMsgId(String.valueOf(flag));
			message.setMsgMsg("삭제 실패.");			
		}
		
		Gson gson=new Gson();
		String gsonStr = gson.toJson(message);
		
		LOG.debug("1=================");
		LOG.debug("1=gsonStr="+gsonStr);
		LOG.debug("1=================");	
		
		
		return gsonStr;
	}
	//
	@RequestMapping(value = "board/do_selectone.do",method = RequestMethod.GET)
	public String doSelectOne(BoardVO  board,Locale locale,Model model) {
		//param board_id
		LOG.debug("1=================");
		LOG.debug("1=param="+board);
		LOG.debug("1=================");
		
		if( 0 == board.getBoardId()) {		

			throw new IllegalArgumentException("Board_ID를 확인 하세요.");			
		}
		
		BoardVO  outVO =(BoardVO) this.boardService.doSelectOne(board);
		LOG.debug("1.1.=================");
		LOG.debug("1.1=outVO="+outVO);
		LOG.debug("1.1=================");	
		model.addAttribute("vo", outVO);
		
		return "board/board_mng";
	}
	
	@RequestMapping(value = "board/do_insert_view.do",method = RequestMethod.GET)
	public String doInsertView(Locale locale) {
		
		LOG.debug("1=================");
		LOG.debug("1=board/board_write");
		LOG.debug("1=================");
		return "board/board_write";
	}
	
	@RequestMapping(value = "board/do_insert.do",method = RequestMethod.POST
			,produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String doInsert(BoardVO  board,Locale locale) {
		LOG.debug("1=================");
		LOG.debug("1=param="+board);
		LOG.debug("1=================");
				
		if(null == board.getTitle() || "".equals(board.getTitle().trim())) {
			//다국어 메시지 처리
			String title = messageSource.getMessage("message.board.search.condition.title"
					 , null, locale);
			Object[] args = new String[]{title};			
			String commMsg = messageSource.getMessage("message.common.message.save", args, locale);
			LOG.debug("1.1=================");
			LOG.debug("1.1=commMsg="+commMsg);
			LOG.debug("1.1=================");			

			throw new IllegalArgumentException(commMsg);
		}
		
		if(null == board.getContents() || "".equals(board.getContents().trim())) {
			//다국어 메시지 처리
			String contents = messageSource.getMessage("message.board.search.condition.content"
					, null, locale);
			Object[] args = new String[]{contents};			
			String commMsg = messageSource.getMessage("message.common.message.save", args, locale);
			LOG.debug("1.1=================");
			LOG.debug("1.1=commMsg="+commMsg);
			LOG.debug("1.1=================");	
			
			throw new IllegalArgumentException(commMsg);
		}		
		
		int flag = this.boardService.doInsert(board);
		
		MessageVO message=new MessageVO();
		
		if(flag>0) {
			message.setMsgId(String.valueOf(flag));
			message.setMsgMsg("등록 성공.");
		}else {
			message.setMsgId(String.valueOf(flag));
			message.setMsgMsg("등록 실패.");			
		}
		
		Gson gson=new Gson();
		String jsonStr = gson.toJson(message);
		LOG.debug("1.2=================");
		LOG.debug("1.2=jsonStr="+jsonStr);
		LOG.debug("1.2=================");		
		return jsonStr;
	}
	
	
	
}















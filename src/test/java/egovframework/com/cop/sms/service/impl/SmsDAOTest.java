package egovframework.com.cop.sms.service.impl;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.exception.FdlException;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.sms.service.Sms;
import egovframework.com.cop.sms.service.SmsRecptn;
import egovframework.com.cop.sms.service.SmsVO;
import egovframework.com.test.EgovTestAbstractDAO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 문자메시지를 위한 데이터 접근 클래스 DAO 단위 테스트
 * 
 * @author 이백행
 * @since 2023-08-10
 */

@ContextConfiguration(classes = { EgovTestAbstractDAO.class, SmsDAOTest.class, })

@Configuration

@ImportResource({

        "classpath*:egovframework/spring/com/idgn/context-idgn-Sms.xml",

})

@ComponentScan(

        useDefaultFilters = false,

        basePackages = {

                "egovframework.com.cop.sms.service.impl",

        },

        includeFilters = {

                @Filter(

                        type = FilterType.ASSIGNABLE_TYPE,

                        classes = {

                                SmsDAO.class,

                        }

                )

        }

)

//@Commit

@NoArgsConstructor
@Slf4j
public class SmsDAOTest extends EgovTestAbstractDAO {

    /**
     * 문자메시지를 위한 데이터 접근 클래스
     */
    @Autowired
    private SmsDAO smsDAO;

    /**
     * egovSmsIdGnrService
     */
    @Autowired
    @Qualifier("egovSmsIdGnrService")
    private EgovIdGnrService egovSmsIdGnrService;

    /**
     * testDataSms
     * 
     * @param sms
     */
    private void testDataSms(final Sms testDataSms) {
        try {
            testDataSms.setSmsId(egovSmsIdGnrService.getNextStringId()); // 문자메시지ID
        } catch (FdlException e) {
//            e.printStackTrace();
            log.error("FdlException egovSmsIdGnrService");
//            fail("FdlException egovSmsIdGnrService");
        }

        testDataSms.setTrnsmitTelno("전송전화번호"); // 전송전화번호
        testDataSms.setTrnsmitCn("test 이백행 전송내용 " + LocalDateTime.now()); // 전송내용

        smsDAO.insertSmsInf(testDataSms);
    }

    /**
     * 문자메시지 목록을 조회한다.
     */
    @Test
    public void selectSmsInfs() {
        // given
        final SmsVO testDataSms = new SmsVO();
        testDataSms(testDataSms);

        final SmsVO smsVO = new SmsVO();

        smsVO.setFirstIndex(0);
        smsVO.setRecordCountPerPage(1);

        smsVO.setSearchCnd(null);

//        smsVO.setSearchCnd("0");
//        smsVO.setSearchWrd("");

        smsVO.setSearchCnd("1");
        smsVO.setSearchWrd(testDataSms.getTrnsmitCn());

        // when
        final List<SmsVO> resultList = smsDAO.selectSmsInfs(smsVO);

        // then
        if (log.isDebugEnabled()) {
            for (final SmsVO result : resultList) {
                log.debug("getSmsId={}, {}", testDataSms.getSmsId(), result.getSmsId());
                log.debug("getTrnsmitTelno={}, {}", testDataSms.getTrnsmitTelno(), result.getTrnsmitTelno());
//                log.debug("getTrnsmitCn={}, {}", testDataSms.getTrnsmitCn(), result.getTrnsmitCn());
            }
        }

        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), testDataSms.getSmsId(),
                resultList.get(0).getSmsId());
        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), testDataSms.getTrnsmitTelno(),
                resultList.get(0).getTrnsmitTelno());
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), testDataSms.getTrnsmitCn(),
//                resultList.get(0).getTrnsmitCn());
    }

    /**
     * 문자메시지 목록 숫자를 조회한다
     */
    @Test
    public void selectSmsInfsCnt() {
        // given
        final SmsVO testDataSms = new SmsVO();
        testDataSms(testDataSms);

        final SmsVO smsVO = new SmsVO();

        smsVO.setSearchCnd(null);

//        smsVO.setSearchCnd("0");
//        smsVO.setSearchWrd("");

        smsVO.setSearchCnd("1");
        smsVO.setSearchWrd(testDataSms.getTrnsmitCn());

        // when
        final int totCnt = smsDAO.selectSmsInfsCnt(smsVO);

        // then
        if (log.isDebugEnabled()) {
            log.debug("totCnt={}", totCnt);
        }

        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), 1, totCnt);
    }

    /**
     * 문자메시지 정보를 등록한다.
     */
    @Test
    public void insertSmsInf() {
        // given
        final Sms sms = new Sms();
        final LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

        try {
            sms.setSmsId(egovSmsIdGnrService.getNextStringId());
        } catch (FdlException e) {
//            e.printStackTrace();
            log.error("FdlException egovSmsIdGnrService");
//            fail("FdlException egovSmsIdGnrService");
        }

        if (loginVO != null) {
            sms.setFrstRegisterId(loginVO.getUniqId());
        }

        sms.setTrnsmitTelno("1566-3598"); // 전송전화번호
        sms.setTrnsmitCn("test 이백행 전송내용 " + LocalDateTime.now()); // 전송내용

        // when
        final int result = smsDAO.insertSmsInf(sms);

        if (log.isDebugEnabled()) {
            log.debug("sms={}", sms);
            log.debug("result={}", result);
        }

        // then
        assertEquals(egovMessageSource.getMessage("fail.common.insert"), 1, result);
    }

    /**
     * 문자메시지 수신정보 및 결과 정보를 등록한다.
     */
    @Test
    public void insertSmsRecptnInf() {
        // given
        final Sms testDataSms = new Sms();
        testDataSms(testDataSms);

        final SmsRecptn smsRecptn = new SmsRecptn();
        smsRecptn.setSmsId(testDataSms.getSmsId()); // 문자메시지ID
        smsRecptn.setRecptnTelno("수신전화번호"); // 수신전화번호
        smsRecptn.setResultCode("결과코드"); // 결과코드
        smsRecptn.setResultMssage("test 이백행 결과메시지 " + LocalDateTime.now()); // 결과메시지

        // when
        final int result = smsDAO.insertSmsRecptnInf(smsRecptn);

        // then
        assertEquals(egovMessageSource.getMessage("fail.common.insert"), 1, result);
    }

}
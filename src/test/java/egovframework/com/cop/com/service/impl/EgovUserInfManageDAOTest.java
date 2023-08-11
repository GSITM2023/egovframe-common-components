package egovframework.com.cop.com.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.com.service.UserInfVO;
import egovframework.com.test.EgovTestAbstractDAO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 협업 활용 사용자 정보 조회를 위한 데이터 접근 클래스 단위 테스트
 * 
 * @author 이백행
 * @since 2023-08-11
 */
@ContextConfiguration(classes = { EgovTestAbstractDAO.class, EgovUserInfManageDAOTest.class, })

@Configuration

@ImportResource({

//        "classpath*:egovframework/spring/com/idgn/context-idgn-Cmmnty.xml",

})

@ComponentScan(

        useDefaultFilters = false,

        basePackages = {

                "egovframework.com.cop.com.service.impl",

        },

        includeFilters = {

                @Filter(

                        type = FilterType.ASSIGNABLE_TYPE,

                        classes = {

                                EgovUserInfManageDAO.class,

                        }

                )

        }

)

// @Commit

@Sql({ "/test-club-data.sql" })

@NoArgsConstructor
@Slf4j
public class EgovUserInfManageDAOTest extends EgovTestAbstractDAO {

    /**
     * 협업 활용 사용자 정보 조회를 위한 데이터 접근 클래스
     */
    @Autowired
    private EgovUserInfManageDAO egovUserInfManageDAO;

//    /**
//     * egovCmmntyIdGnrService
//     */
//    @Autowired
//    @Qualifier("egovCmmntyIdGnrService")
//    private EgovIdGnrService egovCmmntyIdGnrService;

    private void testData(final UserInfVO testDataUserInfVO) {
        testDataUserInfVO.setClubId("TEST_CLB_00000000001");
        testDataUserInfVO.setCmmntyId("TEST_CMMNTY_00000001");

        final LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        if (loginVO != null) {
//            testDataUserInfVO.setUniqId("USRCNFRM_00000000000");
            testDataUserInfVO.setUniqId(loginVO.getUniqId());
            testDataUserInfVO.setUserId(loginVO.getId());
        }

        testDataUserInfVO.setUseAt("Y");
    }

    /**
     * 동호회 사용자 목록을 조회한다.
     */
    @Test
    public void selectClubUserList() {
        // given
        final UserInfVO testDataUserInfVO = new UserInfVO();
        testData(testDataUserInfVO);
        final UserInfVO userVO = new UserInfVO();

        userVO.setTrgetId(testDataUserInfVO.getClubId());
        userVO.setFirstIndex(0);
        userVO.setRecordCountPerPage(10);

        userVO.setSearchCnd(null);

        // when
        List<UserInfVO> resultList = null;
        try {
            resultList = egovUserInfManageDAO.selectClubUserList(userVO);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("Exception selectClubUserList 동호회 사용자 목록을 조회한다.");
            error((DataAccessException) e);
            fail("Exception selectClubUserList 동호회 사용자 목록을 조회한다.");
        }

        // then
        if (log.isDebugEnabled()) {
            for (final UserInfVO result : resultList) {
                log.debug("getUniqId={}, {}", testDataUserInfVO.getUniqId(), result.getUniqId());
                log.debug("getUserId={}, {}", testDataUserInfVO.getUserId(), result.getUserId());
                log.debug("getUseAt={}, {}", testDataUserInfVO.getUseAt(), result.getUseAt());
            }
        }

        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), testDataUserInfVO.getUniqId(),
                resultList.get(0).getUniqId());
        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), testDataUserInfVO.getUserId(),
                resultList.get(0).getUserId());
        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), testDataUserInfVO.getUseAt(),
                resultList.get(0).getUseAt());
    }

}

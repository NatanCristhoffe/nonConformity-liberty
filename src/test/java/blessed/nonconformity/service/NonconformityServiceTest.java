package blessed.nonconformity.service;

import blessed.company.entity.Company;
import blessed.company.service.query.CompanyQuery;
import blessed.exception.BusinessException;
import blessed.nonconformity.dto.NonconformityRequestDTO;
import blessed.nonconformity.dto.NonconformityResponseDTO;
import blessed.nonconformity.entity.NonConformity;
import blessed.nonconformity.enums.NonConformityPriorityLevel;
import blessed.nonconformity.enums.NonConformityStatus;
import blessed.nonconformity.service.query.NonConformityQuery;
import blessed.sector.entity.Sector;
import blessed.sector.service.query.SectorQuery;
import blessed.user.entity.User;
import blessed.user.enums.UserRole;
import blessed.user.service.UserService;
import blessed.user.service.query.UserQuery;
import blessed.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class NonconformityServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SectorQuery sectorQuery;
    @Mock
    private CompanyQuery companyQuery;
    @Mock
    private NonConformityQuery nonConformityQuery;
    @InjectMocks
    private NonconformityService nonconformityService;



    @Test
    @DisplayName("should create non-conformity successfully when everything is ok ")
    void shouldCreateNonConformityWhenDataIsValid() {
        Company company = TestDataFactory.createCompany();
        Sector sector = TestDataFactory.createSector(company);
        User user = TestDataFactory.createUser(company, sector);

        when(userService.getById(user.getId())).thenReturn(user);
        when(sectorQuery.byId(sector.getId(), company.getId())).thenReturn(sector);
        when(companyQuery.byId(company.getId())).thenReturn(company);

        NonconformityRequestDTO request = new NonconformityRequestDTO(
                "Test create",
                "test create non-conformity",
                false,
                NonConformityPriorityLevel.HIGH,
                LocalDateTime.now(),
                null,
                user.getSector().getId(),
                user.getSector().getId(),
                user.getId(),
                user.getId(),
                false,
                null
        );

        NonconformityResponseDTO response =
                nonconformityService.create(request, user, company.getId(), null);

        assertNotNull(response);
        assertEquals("test create", response.title());

        verify(nonConformityQuery, times(1)).save(any());
    }

    @Test
    @DisplayName("should throw Exception when user not permission admin")
    void shouldThrowBusinessExceptionWhenEffectivenessAnalystIsNotAdmin() {
        Company company = TestDataFactory.createCompany();
        Sector sector = TestDataFactory.createSector(company);
        User user = TestDataFactory.createUser(company, sector);
        user.setRole(UserRole.USER);

        NonconformityRequestDTO request = new NonconformityRequestDTO(
                "Test create",
                "test create non-conformity",
                false,
                NonConformityPriorityLevel.HIGH,
                LocalDateTime.now(),
                null,
                user.getSector().getId(),
                user.getSector().getId(),
                user.getId(),
                user.getId(),
                false,
                null
        );

        when(userService.getById(user.getId())).thenReturn(user);
        when(sectorQuery.byId(sector.getId(), company.getId())).thenReturn(sector);
        when(companyQuery.byId(company.getId())).thenReturn(company);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> nonconformityService.create(request, user, company.getId(), null)
        );

        assertEquals(
                "Usuário não possui permissão para realizar a análise de eficácia.",
                exception.getMessage()
        );
    }

    @Test
    void shouldApproveNonConformityWhenUserIsAdmin(){
        Company company = TestDataFactory.createCompany();
        Sector sector = TestDataFactory.createSector(company);
        User user = TestDataFactory.createUser(company, sector);
        NonConformity nc = TestDataFactory.createNonConformity(company, sector, user);

        when(nonConformityQuery.byId(nc.getId(), company.getId())).thenReturn(nc);

        nonconformityService.approve(nc.getId(), user);
        verify(nonConformityQuery, times(1))
                .byId(nc.getId(), company.getId()
        );
        assertEquals(NonConformityStatus.WAITING_ROOT_CAUSE, nc.getStatus());
    }


    @Test
    void shouldSendToCorrectionNonConformityWhenUserIsAdmin(){
        Company company = TestDataFactory.createCompany();
        Sector sector = TestDataFactory.createSector(company);
        User user = TestDataFactory.createUser(company, sector);

        NonConformity nc = TestDataFactory.createNonConformity(company, sector, user);

        when(nonConformityQuery.byId(nc.getId(), company.getId())).thenReturn(nc);

        nonconformityService.sendToCorrection(nc.getId(), user);

        verify(nonConformityQuery, times(1)).byId(nc.getId(), company.getId());
        assertEquals(NonConformityStatus.RETURNED_FOR_CORRECTION, nc.getStatus());
    }
}
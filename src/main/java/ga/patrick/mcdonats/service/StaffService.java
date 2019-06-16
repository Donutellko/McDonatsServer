package ga.patrick.mcdonats.service;

import ga.patrick.mcdonats.domain.Staff;
import ga.patrick.mcdonats.repository.StaffRepository;
import org.springframework.stereotype.Service;

@Service
public class StaffService {

private StaffRepository staffRepository;

public StaffService(StaffRepository staffRepository) {
    this.staffRepository = staffRepository;
}
}

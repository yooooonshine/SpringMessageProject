package practice.message_project.domain.Member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import practice.message_project.domain.Member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}

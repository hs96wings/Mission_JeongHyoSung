package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        if ( member.hasConnectedInstaMember() == false ) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }

        for (LikeablePerson fromLikeablePerson : member.getInstaMember().getFromLikeablePeople()) {
            String toInstaMemberUsername = fromLikeablePerson.getToInstaMember().getUsername();

            if (username.equals(toInstaMemberUsername)) {
                if (attractiveTypeCode == fromLikeablePerson.getAttractiveTypeCode())
                    return RsData.of("F-3", "%s는 이미 등록한 아이디입니다".formatted(username));
                else {
                    LikeablePerson likeablePerson = fromLikeablePerson
                            .toBuilder()
                            .attractiveTypeCode(attractiveTypeCode)
                            .build();
                    likeablePersonRepository.save(likeablePerson);
                    return RsData.of("S-2", "입력하신 인스타유저(%s)의 호감 사유를 변경했습니다".formatted(username));
                }
            }
        }

        if (member.getInstaMember().getFromLikeablePeople().size() >= 10) {
            return RsData.of("F-4", "호감상대는 10명 넘게 등록할 수 없습니다");
        }

        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(member.getInstaMember().getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장
        // 너가 좋아하는 호감표시 생겼어
        fromInstaMember.addFromLikeablePerson(likeablePerson);

        // 너를 좋아하는 호감표시 생겼어
        toInstaMember.addToLikeablePerson(likeablePerson);

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }

    public LikeablePerson findById(long id) {
        return likeablePersonRepository.findById(id).orElse(null);
    }

    @Transactional // delete이므로 붙여줘야 한다
    public RsData<LikeablePerson> delete(Member member, LikeablePerson likeablePerson) {

        if ( member.hasConnectedInstaMember() == false ) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }

        if (likeablePerson != null && likeablePerson.getFromInstaMember() == member.getInstaMember()) {
            likeablePersonRepository.delete(likeablePerson);
            return RsData.of("S-1", "호감상대인 유저(%s)를 삭제했습니다".formatted(likeablePerson.getToInstaMemberUsername()));
        }

        return RsData.of("F-1", "호감상대를 삭제할 수 없습니다");
    }
}

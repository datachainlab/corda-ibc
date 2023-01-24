// Code generated by protoc-gen-gogo. DO NOT EDIT.
// source: ibc/lightclients/corda/v1/genesis.proto

package types

import (
	context "context"
	fmt "fmt"
	grpc1 "github.com/gogo/protobuf/grpc"
	proto "github.com/gogo/protobuf/proto"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
	io "io"
	math "math"
	math_bits "math/bits"
)

// Reference imports to suppress errors if they are not otherwise used.
var _ = proto.Marshal
var _ = fmt.Errorf
var _ = math.Inf

// This is a compile-time assertion to ensure that this generated file
// is compatible with the proto package it is being compiled against.
// A compilation error at this line likely means your copy of the
// proto package needs to be updated.
const _ = proto.GoGoProtoPackageIsVersion3 // please upgrade the proto package

type CreateGenesisRequest struct {
	Participants []*Party `protobuf:"bytes,1,rep,name=participants,proto3" json:"participants,omitempty"`
}

func (m *CreateGenesisRequest) Reset()         { *m = CreateGenesisRequest{} }
func (m *CreateGenesisRequest) String() string { return proto.CompactTextString(m) }
func (*CreateGenesisRequest) ProtoMessage()    {}
func (*CreateGenesisRequest) Descriptor() ([]byte, []int) {
	return fileDescriptor_05f6fdbaebc64fb0, []int{0}
}
func (m *CreateGenesisRequest) XXX_Unmarshal(b []byte) error {
	return m.Unmarshal(b)
}
func (m *CreateGenesisRequest) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	if deterministic {
		return xxx_messageInfo_CreateGenesisRequest.Marshal(b, m, deterministic)
	} else {
		b = b[:cap(b)]
		n, err := m.MarshalToSizedBuffer(b)
		if err != nil {
			return nil, err
		}
		return b[:n], nil
	}
}
func (m *CreateGenesisRequest) XXX_Merge(src proto.Message) {
	xxx_messageInfo_CreateGenesisRequest.Merge(m, src)
}
func (m *CreateGenesisRequest) XXX_Size() int {
	return m.Size()
}
func (m *CreateGenesisRequest) XXX_DiscardUnknown() {
	xxx_messageInfo_CreateGenesisRequest.DiscardUnknown(m)
}

var xxx_messageInfo_CreateGenesisRequest proto.InternalMessageInfo

func (m *CreateGenesisRequest) GetParticipants() []*Party {
	if m != nil {
		return m.Participants
	}
	return nil
}

type CreateGenesisResponse struct {
	BaseId *StateRef `protobuf:"bytes,1,opt,name=base_id,json=baseId,proto3" json:"base_id,omitempty"`
}

func (m *CreateGenesisResponse) Reset()         { *m = CreateGenesisResponse{} }
func (m *CreateGenesisResponse) String() string { return proto.CompactTextString(m) }
func (*CreateGenesisResponse) ProtoMessage()    {}
func (*CreateGenesisResponse) Descriptor() ([]byte, []int) {
	return fileDescriptor_05f6fdbaebc64fb0, []int{1}
}
func (m *CreateGenesisResponse) XXX_Unmarshal(b []byte) error {
	return m.Unmarshal(b)
}
func (m *CreateGenesisResponse) XXX_Marshal(b []byte, deterministic bool) ([]byte, error) {
	if deterministic {
		return xxx_messageInfo_CreateGenesisResponse.Marshal(b, m, deterministic)
	} else {
		b = b[:cap(b)]
		n, err := m.MarshalToSizedBuffer(b)
		if err != nil {
			return nil, err
		}
		return b[:n], nil
	}
}
func (m *CreateGenesisResponse) XXX_Merge(src proto.Message) {
	xxx_messageInfo_CreateGenesisResponse.Merge(m, src)
}
func (m *CreateGenesisResponse) XXX_Size() int {
	return m.Size()
}
func (m *CreateGenesisResponse) XXX_DiscardUnknown() {
	xxx_messageInfo_CreateGenesisResponse.DiscardUnknown(m)
}

var xxx_messageInfo_CreateGenesisResponse proto.InternalMessageInfo

func (m *CreateGenesisResponse) GetBaseId() *StateRef {
	if m != nil {
		return m.BaseId
	}
	return nil
}

func init() {
	proto.RegisterType((*CreateGenesisRequest)(nil), "ibc.lightclients.corda.v1.CreateGenesisRequest")
	proto.RegisterType((*CreateGenesisResponse)(nil), "ibc.lightclients.corda.v1.CreateGenesisResponse")
}

func init() {
	proto.RegisterFile("ibc/lightclients/corda/v1/genesis.proto", fileDescriptor_05f6fdbaebc64fb0)
}

var fileDescriptor_05f6fdbaebc64fb0 = []byte{
	// 312 bytes of a gzipped FileDescriptorProto
	0x1f, 0x8b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0xff, 0x8c, 0x91, 0x31, 0x4b, 0xc3, 0x40,
	0x14, 0xc7, 0x7b, 0x08, 0x15, 0xae, 0xea, 0x10, 0x14, 0x6a, 0x87, 0x50, 0xea, 0x60, 0x41, 0x72,
	0x67, 0xeb, 0xea, 0xa4, 0x82, 0x38, 0x29, 0x29, 0x2e, 0x22, 0xc8, 0xe5, 0xf2, 0x4c, 0x1f, 0xc4,
	0x24, 0xde, 0xbd, 0x96, 0x66, 0xf7, 0x03, 0xf8, 0xb1, 0x1c, 0x3b, 0x3a, 0x4a, 0xf3, 0x45, 0xa4,
	0x49, 0x15, 0x2b, 0xb6, 0xb8, 0xbd, 0xe1, 0xff, 0xff, 0x3d, 0xfe, 0xfc, 0xf8, 0x21, 0x06, 0x5a,
	0xc6, 0x18, 0x0d, 0x49, 0xc7, 0x08, 0x09, 0x59, 0xa9, 0x53, 0x13, 0x2a, 0x39, 0xee, 0xc9, 0x08,
	0x12, 0xb0, 0x68, 0x45, 0x66, 0x52, 0x4a, 0x9d, 0x7d, 0x0c, 0xb4, 0xf8, 0x19, 0x14, 0x65, 0x50,
	0x8c, 0x7b, 0xad, 0xa3, 0xd5, 0x8c, 0xf2, 0xf0, 0x28, 0xcf, 0x60, 0xc1, 0xe9, 0xdc, 0xf3, 0xdd,
	0x73, 0x03, 0x8a, 0xe0, 0xb2, 0xc2, 0xfb, 0xf0, 0x3c, 0x02, 0x4b, 0xce, 0x05, 0xdf, 0xca, 0x94,
	0x21, 0xd4, 0x98, 0xa9, 0x84, 0x6c, 0x93, 0xb5, 0x37, 0xba, 0x8d, 0x7e, 0x5b, 0xac, 0x7c, 0x2b,
	0x6e, 0x94, 0xa1, 0xdc, 0x5f, 0x6a, 0x75, 0x6e, 0xf9, 0xde, 0x2f, 0xba, 0xcd, 0xd2, 0xc4, 0x82,
	0x73, 0xca, 0x37, 0x03, 0x65, 0xe1, 0x01, 0xc3, 0x26, 0x6b, 0xb3, 0x6e, 0xa3, 0x7f, 0xb0, 0x86,
	0x3c, 0x20, 0x45, 0xe0, 0xc3, 0xa3, 0x5f, 0x9f, 0x77, 0xae, 0xc2, 0xfe, 0x0b, 0xe3, 0x3b, 0x0b,
	0xe2, 0x00, 0xcc, 0x18, 0x35, 0x38, 0x86, 0x6f, 0x2f, 0x7d, 0x72, 0xe4, 0x1a, 0xe0, 0x5f, 0x8b,
	0x5b, 0xc7, 0xff, 0x2f, 0x54, 0x23, 0xce, 0xf0, 0x6d, 0xe6, 0xb2, 0xe9, 0xcc, 0x65, 0x1f, 0x33,
	0x97, 0xbd, 0x16, 0x6e, 0x6d, 0x5a, 0xb8, 0xb5, 0xf7, 0xc2, 0xad, 0xdd, 0x5d, 0x47, 0x48, 0xc3,
	0x51, 0x20, 0x74, 0xfa, 0x24, 0x87, 0x79, 0x06, 0x26, 0x86, 0x30, 0x02, 0xe3, 0xc5, 0x2a, 0xb0,
	0x32, 0x1f, 0xa1, 0x57, 0x89, 0x98, 0x8b, 0x8a, 0x52, 0x39, 0x91, 0xdf, 0xc6, 0xbc, 0x2f, 0x65,
	0x93, 0x49, 0x95, 0x91, 0xa5, 0xac, 0xa0, 0x5e, 0xda, 0x3a, 0xf9, 0x0c, 0x00, 0x00, 0xff, 0xff,
	0x34, 0xbb, 0x82, 0x77, 0x20, 0x02, 0x00, 0x00,
}

// Reference imports to suppress errors if they are not otherwise used.
var _ context.Context
var _ grpc.ClientConn

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
const _ = grpc.SupportPackageIsVersion4

// GenesisServiceClient is the client API for GenesisService service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://godoc.org/google.golang.org/grpc#ClientConn.NewStream.
type GenesisServiceClient interface {
	CreateGenesis(ctx context.Context, in *CreateGenesisRequest, opts ...grpc.CallOption) (*CreateGenesisResponse, error)
}

type genesisServiceClient struct {
	cc grpc1.ClientConn
}

func NewGenesisServiceClient(cc grpc1.ClientConn) GenesisServiceClient {
	return &genesisServiceClient{cc}
}

func (c *genesisServiceClient) CreateGenesis(ctx context.Context, in *CreateGenesisRequest, opts ...grpc.CallOption) (*CreateGenesisResponse, error) {
	out := new(CreateGenesisResponse)
	err := c.cc.Invoke(ctx, "/ibc.lightclients.corda.v1.GenesisService/CreateGenesis", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// GenesisServiceServer is the server API for GenesisService service.
type GenesisServiceServer interface {
	CreateGenesis(context.Context, *CreateGenesisRequest) (*CreateGenesisResponse, error)
}

// UnimplementedGenesisServiceServer can be embedded to have forward compatible implementations.
type UnimplementedGenesisServiceServer struct {
}

func (*UnimplementedGenesisServiceServer) CreateGenesis(ctx context.Context, req *CreateGenesisRequest) (*CreateGenesisResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method CreateGenesis not implemented")
}

func RegisterGenesisServiceServer(s grpc1.Server, srv GenesisServiceServer) {
	s.RegisterService(&_GenesisService_serviceDesc, srv)
}

func _GenesisService_CreateGenesis_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(CreateGenesisRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(GenesisServiceServer).CreateGenesis(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/ibc.lightclients.corda.v1.GenesisService/CreateGenesis",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(GenesisServiceServer).CreateGenesis(ctx, req.(*CreateGenesisRequest))
	}
	return interceptor(ctx, in, info, handler)
}

var _GenesisService_serviceDesc = grpc.ServiceDesc{
	ServiceName: "ibc.lightclients.corda.v1.GenesisService",
	HandlerType: (*GenesisServiceServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "CreateGenesis",
			Handler:    _GenesisService_CreateGenesis_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "ibc/lightclients/corda/v1/genesis.proto",
}

func (m *CreateGenesisRequest) Marshal() (dAtA []byte, err error) {
	size := m.Size()
	dAtA = make([]byte, size)
	n, err := m.MarshalToSizedBuffer(dAtA[:size])
	if err != nil {
		return nil, err
	}
	return dAtA[:n], nil
}

func (m *CreateGenesisRequest) MarshalTo(dAtA []byte) (int, error) {
	size := m.Size()
	return m.MarshalToSizedBuffer(dAtA[:size])
}

func (m *CreateGenesisRequest) MarshalToSizedBuffer(dAtA []byte) (int, error) {
	i := len(dAtA)
	_ = i
	var l int
	_ = l
	if len(m.Participants) > 0 {
		for iNdEx := len(m.Participants) - 1; iNdEx >= 0; iNdEx-- {
			{
				size, err := m.Participants[iNdEx].MarshalToSizedBuffer(dAtA[:i])
				if err != nil {
					return 0, err
				}
				i -= size
				i = encodeVarintGenesis(dAtA, i, uint64(size))
			}
			i--
			dAtA[i] = 0xa
		}
	}
	return len(dAtA) - i, nil
}

func (m *CreateGenesisResponse) Marshal() (dAtA []byte, err error) {
	size := m.Size()
	dAtA = make([]byte, size)
	n, err := m.MarshalToSizedBuffer(dAtA[:size])
	if err != nil {
		return nil, err
	}
	return dAtA[:n], nil
}

func (m *CreateGenesisResponse) MarshalTo(dAtA []byte) (int, error) {
	size := m.Size()
	return m.MarshalToSizedBuffer(dAtA[:size])
}

func (m *CreateGenesisResponse) MarshalToSizedBuffer(dAtA []byte) (int, error) {
	i := len(dAtA)
	_ = i
	var l int
	_ = l
	if m.BaseId != nil {
		{
			size, err := m.BaseId.MarshalToSizedBuffer(dAtA[:i])
			if err != nil {
				return 0, err
			}
			i -= size
			i = encodeVarintGenesis(dAtA, i, uint64(size))
		}
		i--
		dAtA[i] = 0xa
	}
	return len(dAtA) - i, nil
}

func encodeVarintGenesis(dAtA []byte, offset int, v uint64) int {
	offset -= sovGenesis(v)
	base := offset
	for v >= 1<<7 {
		dAtA[offset] = uint8(v&0x7f | 0x80)
		v >>= 7
		offset++
	}
	dAtA[offset] = uint8(v)
	return base
}
func (m *CreateGenesisRequest) Size() (n int) {
	if m == nil {
		return 0
	}
	var l int
	_ = l
	if len(m.Participants) > 0 {
		for _, e := range m.Participants {
			l = e.Size()
			n += 1 + l + sovGenesis(uint64(l))
		}
	}
	return n
}

func (m *CreateGenesisResponse) Size() (n int) {
	if m == nil {
		return 0
	}
	var l int
	_ = l
	if m.BaseId != nil {
		l = m.BaseId.Size()
		n += 1 + l + sovGenesis(uint64(l))
	}
	return n
}

func sovGenesis(x uint64) (n int) {
	return (math_bits.Len64(x|1) + 6) / 7
}
func sozGenesis(x uint64) (n int) {
	return sovGenesis(uint64((x << 1) ^ uint64((int64(x) >> 63))))
}
func (m *CreateGenesisRequest) Unmarshal(dAtA []byte) error {
	l := len(dAtA)
	iNdEx := 0
	for iNdEx < l {
		preIndex := iNdEx
		var wire uint64
		for shift := uint(0); ; shift += 7 {
			if shift >= 64 {
				return ErrIntOverflowGenesis
			}
			if iNdEx >= l {
				return io.ErrUnexpectedEOF
			}
			b := dAtA[iNdEx]
			iNdEx++
			wire |= uint64(b&0x7F) << shift
			if b < 0x80 {
				break
			}
		}
		fieldNum := int32(wire >> 3)
		wireType := int(wire & 0x7)
		if wireType == 4 {
			return fmt.Errorf("proto: CreateGenesisRequest: wiretype end group for non-group")
		}
		if fieldNum <= 0 {
			return fmt.Errorf("proto: CreateGenesisRequest: illegal tag %d (wire type %d)", fieldNum, wire)
		}
		switch fieldNum {
		case 1:
			if wireType != 2 {
				return fmt.Errorf("proto: wrong wireType = %d for field Participants", wireType)
			}
			var msglen int
			for shift := uint(0); ; shift += 7 {
				if shift >= 64 {
					return ErrIntOverflowGenesis
				}
				if iNdEx >= l {
					return io.ErrUnexpectedEOF
				}
				b := dAtA[iNdEx]
				iNdEx++
				msglen |= int(b&0x7F) << shift
				if b < 0x80 {
					break
				}
			}
			if msglen < 0 {
				return ErrInvalidLengthGenesis
			}
			postIndex := iNdEx + msglen
			if postIndex < 0 {
				return ErrInvalidLengthGenesis
			}
			if postIndex > l {
				return io.ErrUnexpectedEOF
			}
			m.Participants = append(m.Participants, &Party{})
			if err := m.Participants[len(m.Participants)-1].Unmarshal(dAtA[iNdEx:postIndex]); err != nil {
				return err
			}
			iNdEx = postIndex
		default:
			iNdEx = preIndex
			skippy, err := skipGenesis(dAtA[iNdEx:])
			if err != nil {
				return err
			}
			if (skippy < 0) || (iNdEx+skippy) < 0 {
				return ErrInvalidLengthGenesis
			}
			if (iNdEx + skippy) > l {
				return io.ErrUnexpectedEOF
			}
			iNdEx += skippy
		}
	}

	if iNdEx > l {
		return io.ErrUnexpectedEOF
	}
	return nil
}
func (m *CreateGenesisResponse) Unmarshal(dAtA []byte) error {
	l := len(dAtA)
	iNdEx := 0
	for iNdEx < l {
		preIndex := iNdEx
		var wire uint64
		for shift := uint(0); ; shift += 7 {
			if shift >= 64 {
				return ErrIntOverflowGenesis
			}
			if iNdEx >= l {
				return io.ErrUnexpectedEOF
			}
			b := dAtA[iNdEx]
			iNdEx++
			wire |= uint64(b&0x7F) << shift
			if b < 0x80 {
				break
			}
		}
		fieldNum := int32(wire >> 3)
		wireType := int(wire & 0x7)
		if wireType == 4 {
			return fmt.Errorf("proto: CreateGenesisResponse: wiretype end group for non-group")
		}
		if fieldNum <= 0 {
			return fmt.Errorf("proto: CreateGenesisResponse: illegal tag %d (wire type %d)", fieldNum, wire)
		}
		switch fieldNum {
		case 1:
			if wireType != 2 {
				return fmt.Errorf("proto: wrong wireType = %d for field BaseId", wireType)
			}
			var msglen int
			for shift := uint(0); ; shift += 7 {
				if shift >= 64 {
					return ErrIntOverflowGenesis
				}
				if iNdEx >= l {
					return io.ErrUnexpectedEOF
				}
				b := dAtA[iNdEx]
				iNdEx++
				msglen |= int(b&0x7F) << shift
				if b < 0x80 {
					break
				}
			}
			if msglen < 0 {
				return ErrInvalidLengthGenesis
			}
			postIndex := iNdEx + msglen
			if postIndex < 0 {
				return ErrInvalidLengthGenesis
			}
			if postIndex > l {
				return io.ErrUnexpectedEOF
			}
			if m.BaseId == nil {
				m.BaseId = &StateRef{}
			}
			if err := m.BaseId.Unmarshal(dAtA[iNdEx:postIndex]); err != nil {
				return err
			}
			iNdEx = postIndex
		default:
			iNdEx = preIndex
			skippy, err := skipGenesis(dAtA[iNdEx:])
			if err != nil {
				return err
			}
			if (skippy < 0) || (iNdEx+skippy) < 0 {
				return ErrInvalidLengthGenesis
			}
			if (iNdEx + skippy) > l {
				return io.ErrUnexpectedEOF
			}
			iNdEx += skippy
		}
	}

	if iNdEx > l {
		return io.ErrUnexpectedEOF
	}
	return nil
}
func skipGenesis(dAtA []byte) (n int, err error) {
	l := len(dAtA)
	iNdEx := 0
	depth := 0
	for iNdEx < l {
		var wire uint64
		for shift := uint(0); ; shift += 7 {
			if shift >= 64 {
				return 0, ErrIntOverflowGenesis
			}
			if iNdEx >= l {
				return 0, io.ErrUnexpectedEOF
			}
			b := dAtA[iNdEx]
			iNdEx++
			wire |= (uint64(b) & 0x7F) << shift
			if b < 0x80 {
				break
			}
		}
		wireType := int(wire & 0x7)
		switch wireType {
		case 0:
			for shift := uint(0); ; shift += 7 {
				if shift >= 64 {
					return 0, ErrIntOverflowGenesis
				}
				if iNdEx >= l {
					return 0, io.ErrUnexpectedEOF
				}
				iNdEx++
				if dAtA[iNdEx-1] < 0x80 {
					break
				}
			}
		case 1:
			iNdEx += 8
		case 2:
			var length int
			for shift := uint(0); ; shift += 7 {
				if shift >= 64 {
					return 0, ErrIntOverflowGenesis
				}
				if iNdEx >= l {
					return 0, io.ErrUnexpectedEOF
				}
				b := dAtA[iNdEx]
				iNdEx++
				length |= (int(b) & 0x7F) << shift
				if b < 0x80 {
					break
				}
			}
			if length < 0 {
				return 0, ErrInvalidLengthGenesis
			}
			iNdEx += length
		case 3:
			depth++
		case 4:
			if depth == 0 {
				return 0, ErrUnexpectedEndOfGroupGenesis
			}
			depth--
		case 5:
			iNdEx += 4
		default:
			return 0, fmt.Errorf("proto: illegal wireType %d", wireType)
		}
		if iNdEx < 0 {
			return 0, ErrInvalidLengthGenesis
		}
		if depth == 0 {
			return iNdEx, nil
		}
	}
	return 0, io.ErrUnexpectedEOF
}

var (
	ErrInvalidLengthGenesis        = fmt.Errorf("proto: negative length found during unmarshaling")
	ErrIntOverflowGenesis          = fmt.Errorf("proto: integer overflow")
	ErrUnexpectedEndOfGroupGenesis = fmt.Errorf("proto: unexpected end of group")
)

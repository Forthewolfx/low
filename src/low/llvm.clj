(ns low.llvm
  (:require [low.jna :refer [import-function load-lib]])
  (:import (com.sun.jna Pointer)))

(def ^:private llvm-api
  [[:CreateTargetData Pointer 1]
   [:AddTargetData Void 0]
   [:CopyStringRepOfTargetData Pointer 0]
   [:ByteOrder Integer 0] ;; ValuedEnum
   [:PointerSize Void 0]
   [:IntPtrType Pointer 0]
   [:SizeOfTypeInBits Void 0]
   [:StoreSizeOfType Void 0]
   [:ABISizeOfType Void 0]
   [:ABIAlignmentOfType Void 0]
   [:CallFrameAlignmentOfType Void 0]
   [:PreferredAlignmentOfType Void 0]
   [:PreferredAlignmentOfGlobal Void 1]
   [:ElementAtOffset Void 2]
   [:OffsetOfElement Void 2]
   [:DisposeTargetData Void 0]
   [:InitializeCore Void 1]
   [:InitializeTransformUtils Void 1]
   [:InitializeScalarOpts Void 1]
   [:InitializeInstCombine Void 1]
   [:InitializeInstrumentation Void 1]
   [:InitializeIPA Void 1]
   [:InitializeCodeGen Void 1]
   [:InitializeTarget Void 1]
   [:LinkInJIT Void 0]
   [:LinkInInterpreter Void 0]
   [:CreateGenericValueOfInt Pointer 3]
   [:CreateGenericValueOfPointer Pointer 1]
   [:CreateGenericValueOfFloat Pointer 2]
   [:GenericValueIntWidth Integer 1]
   [:GenericValueToInt Long 2]
   [:GenericValueToPointer Pointer 1]
   [:GenericValueToFloat Double 2]
   [:DisposeGenericValue Void 1]
   [:CreateExecutionEngineForModule Integer 3]
   [:CreateInterpreterForModule Integer 3]
   [:CreateJITCompilerForModule Integer 4]
   [:CreateExecutionEngine Integer 3]
   [:CreateInterpreter Integer 3]
   [:CreateJITCompiler Integer 4]
   [:DisposeExecutionEngine Void 1]
   [:RunStaticConstructors Void 1]
   [:RunStaticDestructors Void 1]
   [:RunFunctionAsMain Integer 5]
   [:RunFunction Pointer 4]
   [:FreeMachineCodeForFunction Void 2]
   [:AddModule Void 2]
   [:AddModuleProvider Void 2]
   [:RemoveModule Integer 4]
   [:RemoveModuleProvider Integer 4]
   [:FindFunction Integer 3]
   [:RecompileAndRelinkFunction Pointer 2]
   [:GetExecutionEngineTargetData Pointer 1]
   [:AddGlobalMapping Void 3]
   [:GetPointerToGlobal Pointer 2]
   [:DisposeMessage Void 1]
   [:ContextCreate Pointer 0]
   [:GetGlobalContext Pointer 0]
   [:ContextDispose Void 1]
   [:GetMDKindIDInContext Void 3]
   [:GetMDKindID Void 2]
   [:ModuleCreateWithName Pointer 1]
   [:ModuleCreateWithNameInContext Pointer 2]
   [:DisposeModule Void 1]
   [:GetDataLayout Pointer 1]
   [:SetDataLayout Void 2]
   [:GetTarget Pointer 1]
   [:SetTarget Void 2]
   [:GetTypeByName Pointer 2]
   [:DumpModule Void 1]
   [:SetModuleInlineAsm Void 2]
   [:GetModuleContext Pointer 1]
   [:GetTypeKind Integer 1] ;; TypeEnum
   [:GetTypeContext Pointer 1]
   [:Int1TypeInContext Pointer 1]
   [:Int8TypeInContext Pointer 1]
   [:Int16TypeInContext Pointer 1]
   [:Int32TypeInContext Pointer 1]
   [:Int64TypeInContext Pointer 1]
   [:IntTypeInContext Pointer 2]
   [:Int1Type Pointer 0]
   [:Int8Type Pointer 0]
   [:Int16Type Pointer 0]
   [:Int32Type Pointer 0]
   [:Int64Type Pointer 0]
   [:IntType Pointer 1]
   [:GetIntTypeWidth Integer 1]
   [:FloatTypeInContext Pointer 1]
   [:DoubleTypeInContext Pointer 1]
   [:X86FP80TypeInContext Pointer 1]
   [:FP128TypeInContext Pointer 1]
   [:PPCFP128TypeInContext Pointer 1]
   [:FloatType Pointer 0]
   [:DoubleType Pointer 0]
   [:X86FP80Type Pointer 0]
   [:FP128Type Pointer 0]
   [:PPCFP128Type Pointer 0]
   [:FunctionType Pointer 4]
   [:IsFunctionVarArg Integer 1]
   [:GetReturnType Pointer 1]
   [:CountParamTypes Integer 1]
   [:GetParamTypes Void 2]
   [:StructTypeInContext Pointer 4]
   [:StructType Pointer 3]
   [:CountStructElementTypes Integer 1]
   [:GetStructElementTypes Void 2]
   [:IsPackedStruct Integer 1]
   [:ArrayType Pointer 2]
   [:PointerType Pointer 2]
   [:VectorType Pointer 2]
   [:GetElementType Pointer 1]
   [:GetArrayLength Integer 1]
   [:GetPointerAddressSpace Integer 1]
   [:GetVectorSize Integer 1]
   [:VoidTypeInContext Pointer 1]
   [:LabelTypeInContext Pointer 1]
   [:X86MMXTypeInContext Pointer 1]
   [:VoidType Pointer 0]
   [:LabelType Pointer 0]
   [:X86MMXType Pointer 0]
   [:TypeOf Pointer 1]
   [:GetValueName Pointer 1]
   [:SetValueName Void 2]
   [:DumpValue Void 1]
   [:ReplaceAllUsesWith Void 2]
   [:HasMetadata Integer 1]
   [:GetMetadata Pointer 2]
   [:SetMetadata Void 3]
   [:IsAArgument Pointer 1]
   [:IsABasicBlock Pointer 1]
   [:IsAInlineAsm Pointer 1]
   [:IsAUser Pointer 1]
   [:IsAConstant Pointer 1]
   [:IsAConstantAggregateZero Pointer 1]
   [:IsAConstantArray Pointer 1]
   [:IsAConstantExpr Pointer 1]
   [:IsAConstantFP Pointer 1]
   [:IsAConstantInt Pointer 1]
   [:IsAConstantPointerNull Pointer 1]
   [:IsAConstantStruct Pointer 1]
   [:IsAConstantVector Pointer 1]
   [:IsAGlobalValue Pointer 1]
   [:IsAFunction Pointer 1]
   [:IsAGlobalAlias Pointer 1]
   [:IsAGlobalVariable Pointer 1]
   [:IsAUndefValue Pointer 1]
   [:IsAInstruction Pointer 1]
   [:IsABinaryOperator Pointer 1]
   [:IsACallInst Pointer 1]
   [:IsAIntrinsicInst Pointer 1]
   [:IsADbgInfoIntrinsic Pointer 1]
   [:IsADbgDeclareInst Pointer 1]
   [:IsAEHSelectorInst Pointer 1]
   [:IsAMemIntrinsic Pointer 1]
   [:IsAMemCpyInst Pointer 1]
   [:IsAMemMoveInst Pointer 1]
   [:IsAMemSetInst Pointer 1]
   [:IsACmpInst Pointer 1]
   [:IsAFCmpInst Pointer 1]
   [:IsAICmpInst Pointer 1]
   [:IsAExtractElementInst Pointer 1]
   [:IsAGetElementPtrInst Pointer 1]
   [:IsAInsertElementInst Pointer 1]
   [:IsAInsertValueInst Pointer 1]
   [:IsAPHINode Pointer 1]
   [:IsASelectInst Pointer 1]
   [:IsAShuffleVectorInst Pointer 1]
   [:IsAStoreInst Pointer 1]
   [:IsATerminatorInst Pointer 1]
   [:IsABranchInst Pointer 1]
   [:IsAInvokeInst Pointer 1]
   [:IsAReturnInst Pointer 1]
   [:IsASwitchInst Pointer 1]
   [:IsAUnreachableInst Pointer 1]
   [:IsAUnaryInstruction Pointer 1]
   [:IsAAllocaInst Pointer 1]
   [:IsACastInst Pointer 1]
   [:IsABitCastInst Pointer 1]
   [:IsAFPExtInst Pointer 1]
   [:IsAFPToSIInst Pointer 1]
   [:IsAFPToUIInst Pointer 1]
   [:IsAFPTruncInst Pointer 1]
   [:IsAIntToPtrInst Pointer 1]
   [:IsAPtrToIntInst Pointer 1]
   [:IsASExtInst Pointer 1]
   [:IsASIToFPInst Pointer 1]
   [:IsATruncInst Pointer 1]
   [:IsAUIToFPInst Pointer 1]
   [:IsAZExtInst Pointer 1]
   [:IsAExtractValueInst Pointer 1]
   [:IsALoadInst Pointer 1]
   [:IsAVAArgInst Pointer 1]
   [:GetFirstUse Pointer 1]
   [:GetNextUse Pointer 1]
   [:GetUser Pointer 1]
   [:GetUsedValue Pointer 1]
   [:GetOperand Pointer 2]
   [:SetOperand Void 3]
   [:GetNumOperands Integer 1]
   [:ConstNull Pointer 1]
   [:ConstAllOnes Pointer 1]
   [:GetUndef Pointer 1]
   [:IsConstant Integer 1]
   [:IsNull Integer 1]
   [:IsUndef Integer 1]
   [:ConstPointerNull Pointer 1]
   [:MDStringInContext Pointer 3]
   [:MDString Pointer 2]
   [:MDNodeInContext Pointer 3]
   [:MDNode Pointer 2]
   [:ConstInt Pointer 3]
   [:ConstIntOfArbitraryPrecision Pointer 3]
   [:ConstIntOfString Pointer 3]
   [:ConstIntOfStringAndSize Pointer 4]
   [:ConstReal Pointer 2]
   [:ConstRealOfString Pointer 2]
   [:ConstRealOfStringAndSize Pointer 3]
   [:ConstIntGetZExtValue Long 1]
   [:ConstIntGetSExtValue Long 1]
   [:ConstStringInContext Pointer 4]
   [:ConstStructInContext Pointer 4]
   [:ConstString Pointer 3]
   [:ConstArray Pointer 3]
   [:ConstStruct Pointer 3]
   [:ConstVector Pointer 2]
   [:GetConstOpcode Integer 1] ;;ValuedEnum
   [:AlignOf Pointer 1]
   [:SizeOf Pointer 1]
   [:ConstNeg Pointer 1]
   [:ConstNSWNeg Pointer 1]
   [:ConstNUWNeg Pointer 1]
   [:ConstFNeg Pointer 1]
   [:ConstNot Pointer 1]
   [:ConstAdd Pointer 2]
   [:ConstNSWAdd Pointer 2]
   [:ConstNUWAdd Pointer 2]
   [:ConstFAdd Pointer 2]
   [:ConstSub Pointer 2]
   [:ConstNSWSub Pointer 2]
   [:ConstNUWSub Pointer 2]
   [:ConstFSub Pointer 2]
   [:ConstMul Pointer 2]
   [:ConstNSWMul Pointer 2]
   [:ConstNUWMul Pointer 2]
   [:ConstFMul Pointer 2]
   [:ConstUDiv Pointer 2]
   [:ConstSDiv Pointer 2]
   [:ConstExactSDiv Pointer 2]
   [:ConstFDiv Pointer 2]
   [:ConstURem Pointer 2]
   [:ConstSRem Pointer 2]
   [:ConstFRem Pointer 2]
   [:ConstAnd Pointer 2]
   [:ConstOr Pointer 2]
   [:ConstXor Pointer 2]
   [:ConstICmp Pointer 3]
   [:ConstFCmp Pointer 3]
   [:ConstShl Pointer 2]
   [:ConstLShr Pointer 2]
   [:ConstAShr Pointer 2]
   [:ConstGEP Pointer 3]
   [:ConstInBoundsGEP Pointer 3]
   [:ConstTrunc Pointer 2]
   [:ConstSExt Pointer 2]
   [:ConstZExt Pointer 2]
   [:ConstFPTrunc Pointer 2]
   [:ConstFPExt Pointer 2]
   [:ConstUIToFP Pointer 2]
   [:ConstSIToFP Pointer 2]
   [:ConstFPToUI Pointer 2]
   [:ConstFPToSI Pointer 2]
   [:ConstPtrToInt Pointer 2]
   [:ConstIntToPtr Pointer 2]
   [:ConstBitCast Pointer 2]
   [:ConstZExtOrBitCast Pointer 2]
   [:ConstSExtOrBitCast Pointer 2]
   [:ConstTruncOrBitCast Pointer 2]
   [:ConstPointerCast Pointer 2]
   [:ConstIntCast Pointer 3]
   [:ConstFPCast Pointer 2]
   [:ConstSelect Pointer 3]
   [:ConstExtractElement Pointer 2]
   [:ConstInsertElement Pointer 3]
   [:ConstShuffleVector Pointer 3]
   [:ConstExtractValue Pointer 3]
   [:ConstInsertValue Pointer 4]
   [:ConstInlineAsm Pointer 5]
   [:BlockAddress Pointer 2]
   [:GetGlobalParent Pointer 1]
   [:IsDeclaration Integer 1]
   [:GetLinkage Integer 1] ;; ValuedEnum
   [:SetLinkage Void 2]
   [:GetSection Pointer 1]
   [:SetSection Void 2]
   [:GetVisibility Integer 1] ;; ValuedEnum
   [:SetVisibility Void 2]
   [:GetAlignment Void 1]
   [:SetAlignment Void 2]
   [:AddGlobal Pointer 3]
   [:AddGlobalInAddressSpace Pointer 4]
   [:GetNamedGlobal Pointer 2]
   [:GetFirstGlobal Pointer 1]
   [:GetLastGlobal Pointer 1]
   [:GetNextGlobal Pointer 1]
   [:GetPreviousGlobal Pointer 1]
   [:DeleteGlobal Void 1]
   [:GetInitializer Pointer 1]
   [:SetInitializer Void 2]
   [:IsThreadLocal Integer 1]
   [:SetThreadLocal Void 2]
   [:IsGlobalConstant Integer 1]
   [:SetGlobalConstant Void 2]
   [:AddAlias Pointer 4]
   [:AddFunction Pointer 3]
   [:GetNamedFunction Pointer 2]
   [:GetFirstFunction Pointer 1]
   [:GetLastFunction Pointer 1]
   [:GetNextFunction Pointer 1]
   [:GetPreviousFunction Pointer 1]
   [:DeleteFunction Void 1]
   [:GetIntrinsicID Void 1]
   [:GetFunctionCallConv Void 1]
   [:SetFunctionCallConv Void 2]
   [:GetGC Pointer 1]
   [:SetGC Void 2]
   [:AddFunctionAttr Void 2]
   [:GetFunctionAttr Integer 1] ;; ValuedEnum
   [:RemoveFunctionAttr Void 2]
   [:CountParams Integer 1]
   [:GetParams Void 2]
   [:GetParam Pointer 2]
   [:GetParamParent Pointer 1]
   [:GetFirstParam Pointer 1]
   [:GetLastParam Pointer 1]
   [:GetNextParam Pointer 1]
   [:GetPreviousParam Pointer 1]
   [:AddAttribute Void 2]
   [:RemoveAttribute Void 2]
   [:GetAttribute Integer 1] ;; ValuedEnum
   [:SetParamAlignment Void 2]
   [:BasicBlockAsValue Pointer 1]
   [:ValueIsBasicBlock Integer 1]
   [:ValueAsBasicBlock Pointer 1]
   [:GetBasicBlockParent Pointer 1]
   [:CountBasicBlocks Integer 1]
   [:GetBasicBlocks Void 2]
   [:GetFirstBasicBlock Pointer 1]
   [:GetLastBasicBlock Pointer 1]
   [:GetNextBasicBlock Pointer 1]
   [:GetPreviousBasicBlock Pointer 1]
   [:GetEntryBasicBlock Pointer 1]
   [:AppendBasicBlockInContext Pointer 3]
   [:InsertBasicBlockInContext Pointer 3]
   [:AppendBasicBlock Pointer 2]
   [:InsertBasicBlock Pointer 2]
   [:DeleteBasicBlock Void 1]
   [:MoveBasicBlockBefore Void 2]
   [:MoveBasicBlockAfter Void 2]
   [:GetInstructionParent Pointer 1]
   [:GetFirstInstruction Pointer 1]
   [:GetLastInstruction Pointer 1]
   [:GetNextInstruction Pointer 1]
   [:GetPreviousInstruction Pointer 1]
   [:SetInstructionCallConv Void 2]
   [:GetInstructionCallConv Integer 1]
   [:AddInstrAttribute Void 3]
   [:RemoveInstrAttribute Void 3]
   [:SetInstrParamAlignment Void 3]
   [:IsTailCall Integer 1]
   [:SetTailCall Void 2]
   [:AddIncoming Void 4]
   [:CountIncoming Integer 1]
   [:GetIncomingValue Pointer 2]
   [:GetIncomingBlock Pointer 2]
   [:CreateBuilderInContext Pointer 1]
   [:CreateBuilder Pointer 0]
   [:PositionBuilder Void 3]
   [:PositionBuilderBefore Void 2]
   [:PositionBuilderAtEnd Void 2]
   [:GetInsertBlock Pointer 1]
   [:ClearInsertionPosition Void 1]
   [:InsertIntoBuilder Void 2]
   [:InsertIntoBuilderWithName Void 3]
   [:DisposeBuilder Void 1]
   [:SetCurrentDebugLocation Void 2]
   [:GetCurrentDebugLocation Pointer 1]
   [:SetInstDebugLocation Void 2]
   [:BuildRetVoid Pointer 1]
   [:BuildRet Pointer 2]
   [:BuildAggregateRet Pointer 3]
   [:BuildBr Pointer 2]
   [:BuildCondBr Pointer 4]
   [:BuildSwitch Pointer 4]
   [:BuildIndirectBr Pointer 3]
   [:BuildInvoke Pointer 7]
   [:BuildUnreachable Pointer 1]
   [:AddCase Void 3]
   [:AddDestination Void 2]
   [:BuildAdd Pointer 4]
   [:BuildNSWAdd Pointer 4]
   [:BuildNUWAdd Pointer 4]
   [:BuildFAdd Pointer 4]
   [:BuildSub Pointer 4]
   [:BuildNSWSub Pointer 4]
   [:BuildNUWSub Pointer 4]
   [:BuildFSub Pointer 4]
   [:BuildMul Pointer 4]
   [:BuildNSWMul Pointer 4]
   [:BuildNUWMul Pointer 4]
   [:BuildFMul Pointer 4]
   [:BuildUDiv Pointer 4]
   [:BuildSDiv Pointer 4]
   [:BuildExactSDiv Pointer 4]
   [:BuildFDiv Pointer 4]
   [:BuildURem Pointer 4]
   [:BuildSRem Pointer 4]
   [:BuildFRem Pointer 4]
   [:BuildShl Pointer 4]
   [:BuildLShr Pointer 4]
   [:BuildAShr Pointer 4]
   [:BuildAnd Pointer 4]
   [:BuildOr Pointer 4]
   [:BuildXor Pointer 4]
   [:BuildBinOp Pointer 5]
   [:BuildNeg Pointer 3]
   [:BuildNSWNeg Pointer 3]
   [:BuildNUWNeg Pointer 3]
   [:BuildFNeg Pointer 3]
   [:BuildNot Pointer 3]
   [:BuildMalloc Pointer 3]
   [:BuildArrayMalloc Pointer 4]
   [:BuildAlloca Pointer 3]
   [:BuildArrayAlloca Pointer 4]
   [:BuildFree Pointer 2]
   [:BuildLoad Pointer 3]
   [:BuildStore Pointer 3]
   [:BuildGEP Pointer 5]
   [:BuildInBoundsGEP Pointer 5]
   [:BuildStructGEP Pointer 4]
   [:BuildGlobalString Pointer 3]
   [:BuildGlobalStringPtr Pointer 3]
   [:BuildTrunc Pointer 4]
   [:BuildZExt Pointer 4]
   [:BuildSExt Pointer 4]
   [:BuildFPToUI Pointer 4]
   [:BuildFPToSI Pointer 4]
   [:BuildUIToFP Pointer 4]
   [:BuildSIToFP Pointer 4]
   [:BuildFPTrunc Pointer 4]
   [:BuildFPExt Pointer 4]
   [:BuildPtrToInt Pointer 4]
   [:BuildIntToPtr Pointer 4]
   [:BuildBitCast Pointer 4]
   [:BuildZExtOrBitCast Pointer 4]
   [:BuildSExtOrBitCast Pointer 4]
   [:BuildTruncOrBitCast Pointer 4]
   [:BuildCast Pointer 5]
   [:BuildPointerCast Pointer 4]
   [:BuildIntCast Pointer 4]
   [:BuildFPCast Pointer 4]
   [:BuildICmp Pointer 5]
   [:BuildFCmp Pointer 5]
   [:BuildPhi Pointer 3]
   [:BuildCall Pointer 5]
   [:BuildSelect Pointer 5]
   [:BuildVAArg Pointer 4]
   [:BuildExtractElement Pointer 4]
   [:BuildInsertElement Pointer 5]
   [:BuildShuffleVector Pointer 5]
   [:BuildExtractValue Pointer 4]
   [:BuildInsertValue Pointer 5]
   [:BuildIsNull Pointer 3]
   [:BuildIsNotNull Pointer 3]
   [:BuildPtrDiff Pointer 4]
   [:CreateModuleProviderForExistingModule Pointer 1]
   [:DisposeModuleProvider Void 1]
   [:CreateMemoryBufferWithContentsOfFile Integer 3]
   [:CreateMemoryBufferWithSTDIN Integer 2]
   [:DisposeMemoryBuffer Void 1]
   [:GetGlobalPassRegistry Pointer 0]
   [:CreatePassManager Pointer 0]
   [:CreateFunctionPassManagerForModule Pointer 1]
   [:CreateFunctionPassManager Pointer 1]
   [:RunPassManager Integer 2]
   [:InitializeFunctionPassManager Integer 1]
   [:RunFunctionPassManager Integer 2]
   [:FinalizeFunctionPassManager Integer 1]
   [:DisposePassManager Void 1]
   [:ParseBitcode Integer 3]
   [:ParseBitcodeInContext Integer 4]
   [:GetBitcodeModuleInContext Integer 4]
   [:GetBitcodeModule Integer 3]
   [:GetBitcodeModuleProviderInContext Integer 4]
   [:GetBitcodeModuleProvider Integer 3]
   [:WriteBitcodeToFile Integer 2]
   [:WriteBitcodeToFD Integer 4]
   [:WriteBitcodeToFileHandle Integer 2]
   [:VerifyModule Integer 3]
   [:VerifyFunction Integer 2]
   [:ViewFunctionCFG Void 1]
   [:ViewFunctionCFGOnly Void 1]
   [:AddArgumentPromotionPass Void 1]
   [:AddConstantMergePass Void 1]
   [:AddDeadArgEliminationPass Void 1]
   [:AddFunctionAttrsPass Void 1]
   [:AddFunctionInliningPass Void 1]
   [:AddGlobalDCEPass Void 1]
   [:AddGlobalOptimizerPass Void 1]
   [:AddIPConstantPropagationPass Void 1]
   [:AddPruneEHPass Void 1]
   [:AddIPSCCPPass Void 1]
   [:AddInternalizePass Void 1]
   [:AddStripDeadPrototypesPass Void 1]
   [:AddStripSymbolsPass Void 1]
   [:AddAggressiveDCEPass Void 1]
   [:AddCFGSimplificationPass Void 1]
   [:AddDeadStoreEliminationPass Void 1]
   [:AddGVNPass Void 1]
   [:AddIndVarSimplifyPass Void 1]
   [:AddInstructionCombiningPass Void 1]
   [:AddJumpThreadingPass Void 1]
   [:AddLICMPass Void 1]
   [:AddLoopDeletionPass Void 1]
   [:AddLoopRotatePass Void 1]
   [:AddLoopUnrollPass Void 1]
   [:AddLoopUnswitchPass Void 1]
   [:AddMemCpyOptPass Void 1]
   [:AddPromoteMemoryToRegisterPass Void 1]
   [:AddReassociatePass Void 1]
   [:AddSCCPPass Void 1]
   [:AddScalarReplAggregatesPass Void 1]
   [:AddScalarReplAggregatesPassWithThreshold Void 2]
   [:AddSimplifyLibCallsPass Void 1]
   [:AddTailCallEliminationPass Void 1]
   [:AddConstantPropagationPass Void 1]
   [:AddDemoteMemoryToRegisterPass Void 1]
   [:AddVerifierPass Void 1]])

(def ^:private llvm-function-map (atom {}))
(def ^:private llvm-lib (promise))

(defn import-llvm-function [f-name ret-type args-len]
  (swap! llvm-function-map assoc f-name
         (import-function @llvm-lib (str "LLVM" (name f-name)) ret-type args-len)))

(defn setup-llvm [ver]
  (deliver llvm-lib (load-lib (str "LLVM-" ver)))
  (doseq [[f-name ret-type args-len] llvm-api]
    (import-llvm-function f-name ret-type args-len)))

(defn LLVM [f & args]
  (if-let [f (@llvm-function-map f)]
    (apply f args)
    (ex-info "Function not found" {:fn-name (str "LLVM" f)})))

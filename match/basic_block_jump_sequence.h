// Copyright 2011-2021 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

#ifndef MATCH_BASIC_BLOCK_JUMP_SEQUENCE_H_
#define MATCH_BASIC_BLOCK_JUMP_SEQUENCE_H_

#include "third_party/zynamics/bindiff/match/flow_graph.h"

namespace security::bindiff {

class MatchingStepJumpSequence : public MatchingStepFlowGraph {
 public:
  MatchingStepJumpSequence()
      : MatchingStepFlowGraph("basicBlock: jump sequence matching",
                              "Basic Block: Jump Sequence") {}

  bool FindFixedPoints(FlowGraph* primary, FlowGraph* secondary,
                       const VertexSet& vertices1, const VertexSet& vertices2,
                       FixedPoint* fixed_point, MatchingContext* context,
                       MatchingStepsFlowGraph* matching_steps) override;

 private:
  void GetUnmatchedBasicBlocksByJumpSequence(const FlowGraph* flow_graph,
                                             const VertexSet& vertices,
                                             VertexIntMap* basic_blocks_map);
};

}  // namespace security::bindiff

#endif  // MATCH_BASIC_BLOCK_JUMP_SEQUENCE_H_